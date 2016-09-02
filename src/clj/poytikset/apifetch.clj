(ns poytikset.apifetch
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clojure.walk :refer [keywordize-keys]]))

(def api "https://api.finna.fi/v1/search?lookfor=kokou*&filter[]=online_boolean:%221%22&filter[]=format:%220/Image/%22")

(defn get-results [] 
 (let [page (inc (rand-int 651))]
   (->> (str api "&page=" page)
        (client/get)
        (:body)
        (json/read-str)
        (keywordize-keys)
        (:records))))

(def resultset (get-results))

(defn get-random-pic []
  (let [res (get-results)
        index (rand-int (count res))
        rec (get res index)
        img (:images rec)
        title (:title rec)]
    (str "http://api.finna.fi" (first img))))
