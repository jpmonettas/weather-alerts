(ns dev
  (:require [clojure.tools.namespace.repl :as tools-namespace-repl]
            [weather-alerts.http-server :as wa-http]))

(defn start-system []
  (println "Starting system...")
  (wa-http/start-server)
  (println "System started."))

(defn stop-system []
  (println "Stopping system...")
  (wa-http/stop-server)
  (println "System stopped."))

(defn after-refresh []
  (println "Refresh done")
  (start-system))

(defn refresh []
  (stop-system)
  (tools-namespace-repl/refresh :after 'dev/after-refresh))

(defn clear-cache []
  (println "Cache cleared."))


(comment

  http://localhost:8080/alerts?lat=-34.90328&lon=-56.18816&min-temp=20&max-temp=25

  (wa-http/handle-alerts user/r)


  user/f
  )
