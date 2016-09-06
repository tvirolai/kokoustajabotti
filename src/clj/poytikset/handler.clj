(ns poytikset.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [poytikset.layout :refer [error-page]]
            [poytikset.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [clojure.string :as string]
            [poytikset.env :refer [defaults]]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]
            [mount.core :as mount]
            [poytikset.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def credentials (zipmap [:name :pass] (string/split-lines (slurp "./credentials.txt"))))

(defn authenticated? [name pass]
  (and (= name (:name credentials))
       (= pass (:pass credentials))))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats)
        (wrap-basic-authentication authenticated?))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(defn app [] (middleware/wrap-base #'app-routes))
