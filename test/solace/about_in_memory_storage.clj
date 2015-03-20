(ns solace.about-in-memory-storage
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]
            [solace.adapters.in-memory-store :refer :all]))

(defn- before-each[f]
  (clear)
  (f))

(use-fixtures :each before-each)

(deftest can-save
  (testing "that you can add moods"
    (save 1) (save 2) (save 3) (save 4) (save 5)
        
    (let [result (list)]
      (is (= 1 (:1 result)))
      (is (= 1 (:2 result)))
      (is (= 1 (:3 result))))))

(deftest can-save-with-either-a-number-or-a-string
  (testing "that you can add moods"
    (save 1) (save "1")
        
    (let [result (list)]
      (is (= 2 (:1 result)), "Expected the count to be <2> because we have added it twice"))))

;; it returns a struct containing the mood and a count
