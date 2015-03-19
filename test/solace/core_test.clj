(ns solace.core-test
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]))

(defn add-solace [persistence n]
  (persistence n))

(def counter (atom 0))

(def fake-persistence
  (fn[n] (swap! counter inc)))

(defn- before-each[]
  (reset! counter 0))

(deftest adding-solace
   (use-fixtures :each before-each)

  (testing "that it notifies persistence"
    (add-solace fake-persistence 1)
    (is (= @counter 1), "Expected that the persistence function would have been called once"))

;; only supports 1-5
;; supports profanity: arse = 1, feck = 2, jubblies = 3, philbo = 4, ricky-bizzness = 5
;; any other tokens are rejected outright
;; persistence not notified when invalid
  )
