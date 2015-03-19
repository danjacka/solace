(ns solace.use-cases.add-solace
  (:refer-clojure :exclude [read]))

(defn- valid?[n]
  (> n 0))

(defn add-solace [persistence n]
  (when (valid? n) 
    (persistence n)))

