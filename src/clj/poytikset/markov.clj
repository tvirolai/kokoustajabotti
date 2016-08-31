(ns poytikset.markov
  (:require [clojure.string :as str]
            [opennlp.nlp :as nlp]))

(def get-sentences
  (nlp/make-sentence-detector "./models/fi-sent.bin"))

(def data (slurp "./data/data.txt"))

(defn markov-data [text]
  (let [maps
        (for [line (get-sentences text)
              m (let [l line
                      words
                      (cons :start (clojure.string/split l #"\s+"))]
                  (for [p (partition 2 1 (remove #(= "" %) words))]
                    {(first p) [(second p)]}))]
          m)]
    (apply merge-with concat maps)))

(defn sentence [data]
  (loop [ws (data :start)
         acc []]
    (let [w (rand-nth ws)
          nws (data w)
          nacc (concat acc [w])]
      (if (= \. (last w))
        (clojure.string/join " " nacc)
        (recur nws nacc)))))

(def parsed-data (markov-data data))

(defn is-original? [sentence]
  (clojure.string/includes? data sentence))

(defn make-sentence []
  (let [x (sentence parsed-data)]
    (if (and 
          (not (is-original? x))
          (> 170 (count x) 10)) 
      x 
      (make-sentence))))
