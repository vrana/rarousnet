(ns rarousnet.weblog.handler
  (:use net.cgrand.enlive-html
        clj-time.coerce
        clj-time.format
        clj-time.local)
  (:require [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET]]
            [ring.util.response :refer [charset]]))

(def articles (read-string (slurp (io/resource "articles.edn"))))
(defn load-article [url]
  ((keyword url) articles))

(def utc-format (formatters :basic-date-time))
(def long-date-format
  (-> (formatter "HH.mm - d. MMMM yyyy")
      (with-locale (new java.util.Locale "cs"))))
(defn permalink [article]
  (str "http://www.rarous.net/weblog/" (:id article) "-" (:url article) ".aspx"))
(defn author-twitter [article]
  (let [author (get article :author)]
    (case author
      "Aleš Roubíček" "@alesroubicek"
      "Alessio Busta" "@alessiobusta"
      nil)))

(deftemplate index-template "weblog/index.html" [])
(deftemplate rss-template "weblog/index.rss" [])
(deftemplate comments-rss-template "weblog/comments.rss" [])
(deftemplate blogpost-template "weblog/blogpost.html" [article]
  [:title] (content (get article :title))
  [[:meta (attr= :name "author")]] (set-attr :content (get article :author))
  [[:meta (attr= :name "description")]] (set-attr :content (get article :description))
  [[:meta (attr= :name "twitter:creator")]] (set-attr :content (author-twitter article))
  [[:meta (attr= :name "twitter:title")]] (set-attr :content (get article :title))
  [[:meta (attr= :name "twitter:description")]] (set-attr :content (get article :description))
  [[:link (attr= :rel "canonical")]] (set-attr :href (permalink article))
  [:article :h1] (content (get article :title))
  [:article :div.entry-content] (html-content (get article :html))
  [:article :p.info :strong] (content (get article :category))
  [:article :strong.user] (content (get article :author))
  [:article (attr= :rel "bookmark")] (set-attr :href (permalink article))
  [:article :p.info :time.published] (content (->> (get article :published)
                                                   from-date
                                                   (unparse long-date-format)))
  [:article :p.info :time.published] (set-attr :datetime (->> (get article :published)
                                                              from-date
                                                              (unparse utc-format))))

(defn render-view [template]
  (-> {:status 200
       :headers {"Content-Type" "text/html"}
       :body (apply str template)}
      (charset "UTF-8")))

(defn render-feed [template]
  (-> {:status 200
       :headers {"Content-Type" "application/xml"}
       :body (apply str template)}
      (charset "UTF-8")))

(defn moved-permanently [location]
  {:status 301
   :headers {"Location" location}})

(defn index []
  (render-view (index-template)))

(defn rss []
  (render-feed (rss-template)))

(defn comments-rss []
  (render-feed (comments-rss-template)))

(defn redirect-to-rss-feed []
   (moved-permanently "http://feeds.feedburner.com/rarous-weblog"))

(defn redirect-to-blogpost [url]
   (moved-permanently (str "http://www.rarous.net/weblog/" url)))

(defn blogpost [url]
  (some-> (load-article url)
          blogpost-template
          render-view))

(defroutes routes
  (GET "/weblog/" [] (index))
  (GET "/weblog/:url" [url] (blogpost url))
  (GET "/feed/rss.ashx" [] (rss))
  (GET "/feed/comments.ashx" [] (comments-rss))

  (GET "/ws/syndikace.asmx/rss" [] (redirect-to-rss-feed))
  (GET "/ws/Syndikace.asmx/Rss" [] (redirect-to-rss-feed))
  (GET "/ws/syndikace.asmx/Rss" [] (redirect-to-rss-feed))
  (GET "/clanek/:url" [url] (redirect-to-blogpost url))
  (GET "/rubrika/:url" [url] (redirect-to-blogpost (subs url (+ 1 (.indexOf url "-")))))
  (GET "/weblog" [] (redirect-to-blogpost ""))
  (GET "/weblog/429-test-driven-developemt-pribeh-nezbedneho-vyvojare.aspx" []
       (moved-permanently "/weblog/429-test-driven-development-pribeh-nezbedneho-vyvojare.aspx")))
