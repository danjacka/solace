(ns solace.integration-tests.about-in-memory-storage
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]
            [solace.adapters.in-memory-store :as store]))

(defn- before-each[f]
  (store/clear)
  (f))

(use-fixtures :each before-each)

(defn- q[mood] (struct quantum mood nil))

(deftest can-save
  (testing "like this"
    (store/save (q 1)) (store/save (q 2)) (store/save (q 3)) (store/save (q 4)) (store/save (q 5))
        
    (let [result (store/list)]
      (is (= 1 (:1 result)))
      (is (= 1 (:2 result)))
      (is (= 1 (:3 result))))))

(deftest can-save-with-either-a-number-or-a-string
  (testing "for example"
    (store/save (q 1)) (store/save (q "1"))
        
    (let [result (store/list)]
      (is (= 2 (:1 result)), "Expected the count to be <2> because we have added it twice"))))

(deftest cannot-save-blank-values
  (testing "like nil"    
    (let [result (store/save (q nil))]
      (is (= true (empty? (store/list))))))
  (testing "or empty string"    
    (let [result (store/save (q (str "")))]
      (is (= true (empty? (store/list))))))
  (testing "or just whitespace"    
    (let [result (store/save (q (str " ")))]
      (is (= true (empty? (store/list)))))))

;; it returns a struct containing the mood and a count
