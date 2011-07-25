(ns clj-ts-viz.views.visuals
  (:require [clj-ts-viz.views.common :as common]
            [noir.content.pages :as pages])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpage "/" []
         (common/layout
           [:p "Welcome to clj-ts-viz"]))

