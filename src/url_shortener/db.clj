(ns url-shortener.db
  (:require [korma.db :as kdb]
            [korma.core :as k]))

(kdb/defdb db (kdb/sqlite3 {:db "url_shortener.db"}))

(k/defentity urls
  (k/pk :short_url)
  (k/table :urls))

(defn init-db []
  (k/exec-raw "create table if not exists urls (short_url text primary key, normal_url text not null)"))

(defn find-url [short-url]
  (let [rows (k/select urls
                       (k/where {:short_url short-url}))]
    (:normal_url (first rows))))

(defn add-url [short-url normal-url]
  (k/insert urls
            (k/values {:short_url short-url :normal_url normal-url})))

(defn update-url [short-url normal-url]
  (k/update urls
            (k/set-fields {:normal_url normal-url})
            (k/where {:short_url short-url})))

(defn delete-url [short-url]
  (k/delete urls
            (k/where {:short_url short-url})))
