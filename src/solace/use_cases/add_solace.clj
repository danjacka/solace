(ns solace.use-cases.add-solace
  (:refer-clojure :exclude [read]))

(def ^:private valid-expletives
  (assoc {} :arse 1 :feck 2 :jubblies 3))

(defn- valid?[n]
  (cond
    (instance? Long n) (and (>= n 1) (<= n 5))
    (instance? String n) (contains? valid-expletives (keyword n))
    :else (throw (Exception. "unsupported message type"))))

(defn- value-for[n]
  (cond
    (instance? Long n) n
    (instance? String n) (get valid-expletives (keyword n))
    :else (throw (Exception. "invalid value passed to VALUE-FOR this should be impossible!"))))

(defn add-solace [persistence n]
  (when (valid? n)
    (persistence (value-for n))))
