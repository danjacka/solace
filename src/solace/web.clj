(ns solace.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [clojure.string]
            [solace.use-cases.add-solace :refer :all]
            [solace.adapters.in-memory-store :as store]))

(def content-type-plain-text {"Content-Type"     "text/plain"})

(def anon {})

(def unauthorized
  {:status  401
   :headers content-type-plain-text 
   :body    "401 Unauthorized"})

(def ok
  {:status  200
   :headers content-type-plain-text 
   :body    "Solace"})

(def created
  {:status  201
   :headers content-type-plain-text 
   :body    "Created"})
   
(defn shell-log[& messages] (println (str "[LOG] " messages)))   
(defn authorise [request who log] true)
(defn- basic-authorise[request, who] (authorise request who shell-log))
(defn- serialize [m] (json/write-str m))

(def persistence
  (fn[n] 
    (println "Saving <" n ">")
    (store/save n)))

(defn- create[mood]
  (add-solace persistence (Integer/parseInt mood))
  (println (str "Storing mood <" mood ">"))
  created)

(defn- find-all[]
  {:status  200
   :headers content-type-plain-text 
   :body    (serialize (store/list)) })

(defroutes app
  (POST "/" [mood] (create mood))
  (GET  "/" [] (find-all))
  (ANY  "*" [] (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
