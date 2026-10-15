(ns url-shortener.server
  (:require [clojure.string :as str]
            [ring.adapter.jetty :as jetty]
            [url-shortener.db :as db]))

(def alphabet "abcdefghijklmnopqrstuvwxyz0123456789")

(defn random-short-url []
  (apply str (repeatedly 8 #(rand-nth alphabet))))

(defn new-short-url []
  (let [short-url (random-short-url)]
    (if (db/find-url short-url)
      (new-short-url)
      short-url)))

(defn decode [text]
  (java.net.URLDecoder/decode text "UTF-8"))

(defn response [status body]
  {:status status
   :headers {"Content-Type" "text/plain; charset=utf-8"}
   :body body})

(defn handle-get [path]
  (let [normal-url (db/find-url (decode path))]
    (if normal-url
      (response 200 normal-url)
      (response 404 "Not found"))))

(defn handle-post [path]
  (if (= path "")
    (response 400 "Bad request")
    (let [short-url (new-short-url)]
      (db/add-url short-url (decode path))
      (response 200 short-url))))

(defn handle-put [path]
  (let [parts (str/split path #"/" 2)]
    (if (and (= (count parts) 2) (not= (second parts) ""))
      (let [short-url (decode (first parts))
            normal-url (decode (second parts))]
        (if (db/find-url short-url)
          (do (db/update-url short-url normal-url)
              (response 200 "OK"))
          (response 404 "Not found")))
      (response 400 "Bad request"))))

(defn handle-delete [path]
  (let [short-url (decode path)]
    (if (db/find-url short-url)
      (do (db/delete-url short-url)
          (response 200 "OK"))
      (response 404 "Not found"))))

(defn handler [request]
  (let [path (subs (:uri request) 1)]
    (case (:request-method request)
      :get (handle-get path)
      :post (handle-post path)
      :put (handle-put path)
      :delete (handle-delete path)
      (response 405 "Method not allowed"))))

(defn start [port]
  (db/init-db)
  (println "Сервер запущен на порту" port)
  (jetty/run-jetty handler {:port port :join? true}))
