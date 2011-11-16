(ns stemmer.util
  "General utility functions for creating stemmers."
  (:require [clojure.string :as str]))

(defn longest-match [re s]
  (if-let [matches (re-seq re s)]
    (apply max-key count matches)))

(defn replace-longest [s & rules]
  (->> (partition 2 rules)
       (apply max-key (fn [[re _]] (count (longest-match re s))))
       (apply str/replace s)))
