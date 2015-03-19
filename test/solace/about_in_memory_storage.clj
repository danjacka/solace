(ns solace.about-in-memory-storage
  (:require [clojure.test :refer :all]
            [solace.core :refer :all]
            [solace.use-cases.add-solace :refer :all]))

(defstruct quantum :mood)

(def ^{:private true} store
  (atom {}))

(defn save[n]
  
)

(defn list-solace[]
  (list)
  )

(deftest can-sav
  (testing "that it calls it once exactly"
    (save 1)
    (let [result (list-solace)]
      (is (= 1 (count result)))
      )))

;; it returns a struct containing the mood and a count
