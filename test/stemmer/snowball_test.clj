(ns stemmer.snowball-test
  (:use clojure.test
        stemmer.snowball))

(deftest test-stemmer
  (let [st (stemmer :english)]
    (is (= (st "loved") (st "love")))
    (is (= (st "fitted") (st "fit")))
    (is (= (st "probably") (st "probable")))))
