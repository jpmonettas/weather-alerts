(ns weather-alerts.weather
  (:require [org.httpkit.client :as http-cli]
            [clojure.data.json :as json]))

(def open-weather-api "<API_KEY_HERE>")

(defn get-weather-forecast

  "Obtain weather forecast by lat lon"

  [[lat lon]]
  (let [url (format "https://api.openweathermap.org/data/2.5/forecast?units=metric&lat=%s&lon=%s&appid=%s"
                    lat lon open-weather-api)
        {:keys [status body]} @(http-cli/get url)]
    (if (= status 200)
      (json/read-str body :key-fn keyword)
      (throw (ex-info "Error obtaining weather" {:resp-body body})))))

(defn alerts

  "Create alerts from weather forecast"

  [{:keys [list] :as forecast} [min-temp max-temp]]

  (->> list
       (keep (fn [{:keys [dt_txt main]}]
               (let [{:keys [temp_min temp_max]} main]
                 (when-not (<= min-temp temp_min temp_max max-temp)
                   {:alert/type :temp
                    :date dt_txt
                    :temp-min temp_min
                    :temp-max temp_max}))))))
