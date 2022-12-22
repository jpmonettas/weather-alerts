(ns weather-alerts.weather
  (:require [org.httpkit.client :as http-cli]
            [clojure.data.json :as json]))

(def open-weather-api "<API_KEY_HERE>")
(def open-weather-endpoint "https://api.openweathermap.org/data/2.5/")

(defn get-weather-forecast

  "Obtain weather forecast by LAT,LON"

  [[lat lon]]
  (let [url (str open-weather-endpoint
                 "forecast"
                 "?"
                 (http-cli/query-string {:units "metric"
                                         :lat lat
                                         :lon lon
                                         :appid open-weather-api}))
        {:keys [status body]} @(http-cli/get url)]
    (if (= status 200)
      (json/read-str body :key-fn keyword)
      (throw (ex-info "Error obtaining weather" {:resp-body body})))))

(defn alerts

  "Create alerts from a weather FORECAST and a MIN-TEMP MAX-TEMP range."

  [{:keys [list] :as forecast} [min-temp max-temp]]

  (->> list
       (keep (fn make-alert? [{:keys [dt_txt main]}]
               (let [{:keys [temp_min temp_max]} main]
                 (when-not (<= min-temp temp_min temp_max max-temp)
                   {:alert/type :temp
                    :date dt_txt
                    :temp-min temp_min
                    :temp-max temp_max}))))
       (into [])))
