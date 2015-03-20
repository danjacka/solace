(ns solace.adapters.in-memory-store
  (:refer-clojure :exclude [list]))

(def ^{:private true} store
  (atom {}))

(defn update [current what]
  (let [n (keyword (str what))]
    (let [current-n-value (get current n)]
      (cond 
        (nil? current-n-value) (merge current {n 1}) 
        :else (update-in current [n] inc)))))

(defn save[what] (swap! store (fn [current] (update current what))))

(defn list[] @store)

(defn clear [] (reset! store {}))
