(ns weather-alerts.integration-test
  (:require  [clojure.test :refer [testing is deftest]]
             [weather-alerts.http-server :as sut]
             [weather-alerts.weather :as weather]
             [taipei-404.html :refer [html->hiccup]]
             [org.httpkit.client :as http-cli]))

(def open-weather-forcast-example-response
  {:status 200
   :body "{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[{\"dt\":1671721200,\"main\":{\"temp\":23.26,\"feels_like\":23.13,\"temp_min\":23.26,\"temp_max\":25.83,\"pressure\":1015,\"sea_level\":1015,\"grnd_level\":1010,\"humidity\":57,\"temp_kf\":-2.57},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":{\"all\":22},\"wind\":{\"speed\":4.08,\"deg\":87,\"gust\":4.95},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-12-22 15:00:00\"},{\"dt\":1671732000,\"main\":{\"temp\":24.14,\"feels_like\":24.12,\"temp_min\":24.14,\"temp_max\":25.22,\"pressure\":1013,\"sea_level\":1013,\"grnd_level\":1008,\"humidity\":58,\"temp_kf\":-1.08},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":{\"all\":24},\"wind\":{\"speed\":9.13,\"deg\":106,\"gust\":9.73},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-12-22 18:00:00\"},{\"dt\":1671742800,\"main\":{\"temp\":23.91,\"feels_like\":24,\"temp_min\":23.91,\"temp_max\":23.91,\"pressure\":1010,\"sea_level\":1010,\"grnd_level\":1006,\"humidity\":63,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":6},\"wind\":{\"speed\":10.38,\"deg\":106,\"gust\":13.6},\"visibility\":10000,\"pop\":0,\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-12-22 21:00:00\"}],\"city\":{\"id\":3442926,\"name\":\"Cordon\",\"coord\":{\"lat\":-34.9033,\"lon\":-56.1882},\"country\":\"UY\",\"population\":0,\"timezone\":-10800,\"sunrise\":1671697693,\"sunset\":1671749902}}"})

(deftest alert-rendering-integration
  (testing "Alert rendering integration test"
    (with-redefs [http-cli/get (constantly (delay open-weather-forcast-example-response))]
      (let [{:keys [body]} (sut/all-routes {:query-params
                                            {"lat" "-34.90328",
                                             "lon" "-56.18816",
                                             "min-temp" "20",
                                             "max-temp" "25"},
                                            :uri "/alerts",
                                            :request-method :get})]
        (is (= [:html
                [:body
                 [:h1 "Alerts"]
                 [:div
                  [:div
                   {:style "margin: 5px; padding: 5px; border: 1px solid red;"}
                   [:div "Alert type : :temp"]
                   [:div "Date : 2022-12-22 15:00:00"]
                   [:div "Min temp : 23.26"]
                   [:div "Max temp : 25.83"]]
                  [:div
                   {:style "margin: 5px; padding: 5px; border: 1px solid red;"}
                   [:div "Alert type : :temp"]
                   [:div "Date : 2022-12-22 18:00:00"]
                   [:div "Min temp : 24.14"]
                   [:div "Max temp : 25.22"]]]]]
               (first (html->hiccup body)))
            "Should render alerts correctly")))))
