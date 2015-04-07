(ns solace.core-test
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]
            [solace.use-cases.add-solace :refer :all]))

(def counter (atom 0))
(def values-seen (atom nil))

(def fake-persistence
  (fn[n]
    (do
      (swap! counter inc)
      (swap! values-seen #(cons n %)))))

(defn- before-each[f]
  (reset! counter 0)
  (reset! values-seen nil)
  (f))

(use-fixtures :each before-each)

(deftest adding-solace-returns-true-on-success
  (testing "for example, the positive case"
    (let [ok? (add-solace fake-persistence 1)]
      (is (= true ok?), "Expected a valid mood to return true")))
      
  (testing "for example, the negative case"
    (let [ok? (add-solace fake-persistence 666)]
      (is (= false ok?), "Expected an invalid mood to return false"))))

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

(deftest but-it-does-support-values-between-one-and-five
  (testing "for example 3"
    (add-solace fake-persistence 3)
    (is (= @counter 1)))
  (testing "1 will also work (and sum the persistence counter)"
    (add-solace fake-persistence 1)
    (is (= @counter 2))))

;; supports profanity: arse = 1, feck = 2, jubblies = 3, philbo = 4, ricky-bizzness = 5
(deftest when-ben-has-to-buy-a-coffee-and-they-tell-him-the-price-he-sez
  (testing "feck"
    (add-solace fake-persistence "feck")
    (is (= @counter 1))
    (is (= (first @values-seen) 2)))
  (testing "arse"
    (add-solace fake-persistence "arse")
    (is (= @counter 2))
    (is (= (nth @values-seen 1)) 1))
  (testing "but not some other phrase")
    (add-solace fake-persistence "actually, that seems reasonable")
    (is (= @counter 2)))

(deftest when-you-finally-find-the-docs-for-the-keyword-in-clojure-thats-nothing-like-its-scheme-equivalent
  (testing "jubblies"
    (add-solace fake-persistence "jubblies")
    (is (= @counter 1))
    (is (= (first @values-seen) 3))))


;; any other tokens are rejected outright
;; persistence not notified when invalid
