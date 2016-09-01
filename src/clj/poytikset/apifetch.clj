(ns poytikset.apifetch
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clojure.walk :refer [keywordize-keys]]))

(def api-query "https://api.finna.fi/v1/search?lookfor=kokous&filter[]=online_boolean:%221%22&filter[]=format:%220/Image/%22")

(def t (:records (keywordize-keys (json/read-str (:body (client/get api-query))))))

(defn get-random-pic []
  (let [index (rand-int (count t))
        rec (get t index)
        img (:images rec)]
    (str "http://api.finna.fi" (first img))))
