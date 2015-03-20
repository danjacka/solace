(ns solace.about-in-memory-storage
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]
            [solace.use-cases.add-solace :refer :all]))

(def ^{:private true} store
  (atom {}))

(defn update [current what]
  (let [n (keyword (str what))]
    (let [current-n-value (get current n)]
      (cond 
        (nil? current-n-value) (merge current {n 1}) 
        :else (update-in current [n] inc)))))

(defn save[what] (swap! store (fn [current] (update current what))))

(defn list-solace[] @store)

(defn- before-each[f]
  (reset! store {})
  (f))

(use-fixtures :each before-each)

(deftest can-save
  (testing "that you can add moods"
    (save 1) (save 2) (save 3) (save 4) (save 5)
        
    (let [result (list-solace)]
      (is (= 1 (:1 result)))
      (is (= 1 (:2 result)))
      (is (= 1 (:3 result))))))

(deftest can-save-with-either-a-number-or-a-string
  (testing "that you can add moods"
    (save 1) (save "1")
        
    (let [result (list-solace)]
      (is (= 2 (:1 result)), "Expected the count to be <2> because we have added it twice"))))

;; it returns a struct containing the mood and a count
