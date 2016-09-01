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

;; -------------------------
;; Application state

(def sent (r/atom ""))

(def pic (r/atom ""))

(def timer (r/atom 0))

;; -------------------------
;; Data fetching

(defn new-sent! []
  (go
    (let [response (<! (http/get "/generate"))]
      (reset! sent (:body response)))))

(defn new-pic! []
  (go
    (let [picture (<! (http/get "/picture"))
          url (:body picture)]
      (reset! pic url))))

(defn cycle-sent []
  (go
    (loop [i 0]
      (new-sent!)
      (new-pic!)
      (<! (timeout 15000))
      (recur (inc i)))))

(defn inc-time! []
  (go
    (loop [i 0]
      (swap! timer inc)
      (<! (timeout 1000))
      (recur (inc i)))))

(defn reset-time! []
  (reset! timer 0))

;; -------------------------

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-default
       [:div.container
        [:a.navbar-brand "Kokoustajarobotti"]
        [:div.navbar-right
         [:button.btn.btn-info.navbar-btn
          {:on-click #(new-sent!)} "Kokousta lisää!"]]]])))

(defn home-page []
  [:div.container
   [:div.jumbotron
    [:h3 @sent]]])

(defn picture-area []
  [:div.container
   [:div.jumbotron
    [:img.center-block {:src @pic}]]])

(def pages
  {:home #'home-page})

(defn page []
  [(pages (session/get :page))])

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
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'picture-area] (.getElementById js/document "picture"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (inc-time!)
  (cycle-sent)
  (mount-components))
