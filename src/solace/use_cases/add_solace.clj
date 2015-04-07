(ns solace.use-cases.add-solace
  (:refer-clojure :exclude [read]))

(def ^:private valid-expletives
  (assoc {} :arse 1 :feck 2 :jubblies 3))

(defn- parse[n]
  (if (number? n) n (Integer/parseInt n))) 
  
(defn numeric-string?[n]
  (try
     (Integer/parseInt (str n))
     true
     (catch Exception e false))) 
  
(defn numeric?[n] 
  (if (number? n) true (numeric-string? n)))  
  
(defn- valid?[n]
  (cond
    (numeric? n) (let [number (parse n)]( and (>= number 1) (<= number 5)))
    (instance? String n) (contains? valid-expletives (keyword n))
    :else (throw (Exception. "unsupported message type"))))

(defn- value-for[n]
  (cond
    (numeric? n) (parse n)
    (instance? String n) (get valid-expletives (keyword n))
    :else (throw (Exception. "invalid value passed to VALUE-FOR this should be impossible!"))))

(defn add-solace [persistence n]
  (when (valid? n)
    (persistence (value-for n)))
    (valid? n))
