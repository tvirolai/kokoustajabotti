(ns poytikset.app
  (:require [poytikset.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
