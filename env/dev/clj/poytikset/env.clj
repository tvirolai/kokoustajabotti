(ns poytikset.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [poytikset.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[poytikset started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[poytikset has shut down successfully]=-"))
   :middleware wrap-dev})
