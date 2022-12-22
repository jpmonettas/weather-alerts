(ns weather-alerts.http-server
  (:require [org.httpkit.server :as http-server]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [GET routes]]
            [hiccup.core :refer [html]]
            [weather-alerts.weather :as weather]))

(defonce cache nil)
(defonce server nil)

(defn render-alerts

  "Render ALERTS into html"

  [alerts]
  (html [:html
         [:body
          [:h1 "Alerts"]
          [:div
           (for [{:keys [alert/type date temp-min temp-max]} alerts]
             [:div {:style "margin: 5px; padding: 5px; border: 1px solid red;"}
              [:div (str "Alert type : " type)]
              [:div (str "Date : " date)]
              [:div (str "Min temp : " temp-min)]
              [:div (str "Max temp : " temp-max)]])]]]))

(defn location

  "Parse location from the request"

  [{:keys [query-params]}]
  [(parse-double (get query-params "lat"))
   (parse-double (get query-params "lon"))])

(defn temp-range

  "Parse temperature range from the request"

  [{:keys [query-params]}]
  [(parse-double (get query-params "min-temp"))
   (parse-double (get query-params "max-temp"))])

(defn handle-alerts

  "Handle requests to /alerts"

  [req]

  (let [loc (location req)
        forecast (weather/get-weather-forecast loc)
        alerts (weather/alerts forecast (temp-range req))]
    (render-alerts alerts)))

(def all-routes
  (routes
   (GET "/alerts" [] handle-alerts)))

(defn start-server []
  (alter-var-root
   #'server
   (constantly (http-server/run-server (wrap-params #'all-routes) {:port 8080}))))

(defn stop-server []
  (when server
    (server)))
