(ns url-shortener.client
  (:require [clj-http.client :as http]))

(def base-url "http://localhost:8080")

(defn encode [text]
  (java.net.URLEncoder/encode text "UTF-8"))

(defn send-request [method path]
  (try
    (http/request {:method method
                   :url (str base-url "/" path)
                   :throw-exceptions false})
    (catch Exception e
      {:status 0 :body ""})))

(defn create-url [normal-url]
  (let [response (send-request :post (encode normal-url))]
    (if (= (:status response) 200)
      (:body response)
      "Ошибка")))

(defn show-url [short-url]
  (let [response (send-request :get (encode short-url))]
    (if (= (:status response) 200)
      (:body response)
      "Ошибка")))

(defn change-url [short-url normal-url]
  (let [response (send-request :put (str (encode short-url) "/" (encode normal-url)))]
    (if (= (:status response) 200)
      "OK"
      "Ошибка")))

(defn delete-url [short-url]
  (let [response (send-request :delete (encode short-url))]
    (if (= (:status response) 200)
      "OK"
      "Ошибка")))
