(ns url-shortener.cli
  (:require [clojure.string :as str]
            [url-shortener.client :as client]))

(defn show-menu []
  (println)
  (println "=== URL Shortener ===")
  (println "1. Создать")
  (println "2. Показать")
  (println "3. Изменить")
  (println "4. Удалить")
  (println "5. Выйти")
  (print "Выберите действие: ")
  (flush))

(defn ask [text]
  (println text)
  (print "  > ")
  (flush)
  (str/trim (or (read-line) "")))

(defn do-create []
  (let [url (ask "Введите обычный URL для сокращения:")]
    (println)
    (println "Отправка запроса...")
    (println (str "Ответ: " (client/create-url url)))))

(defn do-show []
  (let [url (ask "Введите короткий URL:")]
    (println)
    (println "Отправка запроса...")
    (println (str "Ответ: " (client/show-url url)))))

(defn do-change []
  (let [line (ask "Введите через пробел короткий URL и обычный URL:")
        parts (str/split line #"\s+")]
    (println)
    (println "Отправка запроса...")
    (if (= (count parts) 2)
      (println (str "Ответ: " (client/change-url (first parts) (second parts))))
      (println "Ответ: Ошибка"))))

(defn do-delete []
  (let [url (ask "Введите короткий URL:")]
    (println)
    (println "Отправка запроса...")
    (println (str "Ответ: " (client/delete-url url)))))

(defn run []
  (println "Клиент URL Shortener")
  (loop []
    (show-menu)
    (let [choice (str/trim (or (read-line) "5"))]
      (cond
        (= choice "1") (do (do-create) (recur))
        (= choice "2") (do (do-show) (recur))
        (= choice "3") (do (do-change) (recur))
        (= choice "4") (do (do-delete) (recur))
        (= choice "5") (println "До свидания!")
        :else (do (println "Неизвестное действие") (recur))))))
