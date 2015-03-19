(ns solace.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [solace.use-cases.add-solace :refer :all]))

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

(defn authorise [request who log]
  true)

(defn- basic-authorise[request, who]
    (authorise request who shell-log))
   
(defn authorise-as [request, who]
    (if (basic-authorise request who)
        ok
        unauthorized))

(def fake-persistence
  (fn[n] (println "Saving <%s>", n)))

(defn- create[mood]
  (add-solace fake-persistence (Integer/parseInt mood))
  (println "Storing mood <" mood ">")
  created)

(defroutes app
  (POST "/" [mood] (create mood))
  (GET  "/" request (authorise-as request anon))
  (ANY  "*" [] (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
