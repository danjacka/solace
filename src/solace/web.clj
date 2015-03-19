(ns solace.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

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
   
(defn shell-log[& messages] (println (str "[LOG] " messages)))   

(defn authorise [request who log]
  true)

(defn- basic-authorise[request, who]
    (authorise request who shell-log))
   
(defn authorise-as [request, who]
    (if (basic-authorise request who)
        ok
        unauthorized))
   
(defroutes app
  (GET "/" request (authorise-as request anon))
  (ANY "*" [] (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
