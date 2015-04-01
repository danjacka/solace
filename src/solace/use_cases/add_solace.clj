(ns solace.use-cases.add-solace
  (:refer-clojure :exclude [read]))

(defn- valid?[n]
  (and (>= n 1) (<= n 5)))

(defn add-solace [persistence n]
  (when (valid? n)
    (persistence n)))
