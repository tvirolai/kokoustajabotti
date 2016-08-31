(ns poytikset.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[poytikset started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[poytikset has shut down successfully]=-"))
   :middleware identity})
