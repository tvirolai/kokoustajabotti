(ns poytikset.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! timeout]]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [poytikset.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(def sent (r/atom ""))

(defn home-page []
  [:div.container
   [:div.jumbotron.vertical-center
    [:h3 @sent]]])

(def pages
  {:home #'home-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Data fetching

(defn new-sent []
  (go
    (loop [i 0]
      (let [response (<! (http/get "/generate"))]
        (reset! sent (:body response))
        (<! (timeout 10000))
        (recur (inc i))))))

;; -------------------------

;; -------------------------
;; Routes
;(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
;(defn fetch-docs! []
;  (GET (str js/context "/docs") {:handler #(session/put! :docs %)}))

(defn mount-components []
  ;(r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  ;(load-interceptors!)
  ;(fetch-docs!)
  (hook-browser-navigation!)
  (new-sent)
  (mount-components))
