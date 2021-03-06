(ns poytikset.routes.home
  (:require [poytikset.layout :as layout]
            [poytikset.markov :as markov]
            [poytikset.apifetch :as apifetch]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/generate" [] (markov/make-sentence))
  (GET "/picture" [] (apifetch/get-random-pic)))
