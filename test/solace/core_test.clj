(ns solace.core-test
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]
            [solace.use-cases.add-solace :refer :all]))

(def counter (atom 0))

(def fake-persistence
  (fn[n] (swap! counter inc)))

(defn- before-each[f]
  (reset! counter 0)
  (f))

(use-fixtures :each before-each)

(deftest adding-solace-notifies-persistence
  (testing "that it calls it once exactly"
    (add-solace fake-persistence 1)
    (is (= @counter 1), "Expected that the persistence function would have been called once")))

(deftest it-does-not-save-invalid-values
  (testing "for example -1"
    (add-solace fake-persistence -1)
    (is (= @counter 0), "Expected that the persistence function would NOT have been called at all because the value supplied is not valid")))

;; only supports 1-5
(deftest it-does-not-support-numeric-values-above-five
  (testing "for example 6"
    (add-solace fake-persistence 6)
    (is (= @counter 0), "Expected NOT to persist a value above five")))

(deftest but-it-does-support-values-between-one-and-fice
  (testing "for example 3"
    (add-solace fake-persistence 3)
    (is (= @counter 1)))
  (testing "1 will also work (and sum the persistence counter)"
    (add-solace fake-persistence 1)
    (is (= @counter 2))))

;; supports profanity: arse = 1, feck = 2, jubblies = 3, philbo = 4, ricky-bizzness = 5
;; any other tokens are rejected outright
;; persistence not notified when invalid
