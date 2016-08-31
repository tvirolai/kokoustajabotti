(ns user
  (:require [mount.core :as mount]
            [poytikset.figwheel :refer [start-fw stop-fw cljs]]
            poytikset.core))

(defn start []
  (mount/start-without #'poytikset.core/repl-server))

(defn stop []
  (mount/stop-except #'poytikset.core/repl-server))

(defn restart []
  (stop)
  (start))


