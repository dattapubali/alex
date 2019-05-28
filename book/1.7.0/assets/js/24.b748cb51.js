(window.webpackJsonp=window.webpackJsonp||[]).push([[24],{306:function(t,e,r){"use strict";r.r(e);var a=r(0),s=Object(a.a)({},function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[r("h1",{attrs:{id:"wordpress"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#wordpress","aria-hidden":"true"}},[t._v("#")]),t._v(" Wordpress")]),t._v(" "),r("p",[t._v("In this section, we demonstrate how to learn the "),r("a",{attrs:{href:"https://developer.wordpress.org/rest-api/",target:"_blank",rel:"noopener noreferrer"}},[t._v("REST API"),r("OutboundLink")],1),t._v(" of the content management system "),r("a",{attrs:{href:"https://wordpress.org",target:"_blank",rel:"noopener noreferrer"}},[t._v("Wordpress"),r("OutboundLink")],1),t._v(" v4.9.8.\nThis tutorial covers the article publishing process using different user roles that Wordpress offers.\nThe modelled symbols can be downloaded "),r("a",{attrs:{href:"./assets/symbols-wordpress-20181007.json"}},[t._v("here")]),t._v(" and imported into ALEX.")]),t._v(" "),r("h2",{attrs:{id:"preparation"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#preparation","aria-hidden":"true"}},[t._v("#")]),t._v(" Preparation")]),t._v(" "),r("h3",{attrs:{id:"installation"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#installation","aria-hidden":"true"}},[t._v("#")]),t._v(" Installation")]),t._v(" "),r("ol",[r("li",[t._v("Install PHP and MySQL, e.g. via XAMPP, LAMPP etc.")]),t._v(" "),r("li",[t._v("Install and setup Wordpress v4.9.8 using the admin account as seen the table below.")]),t._v(" "),r("li",[t._v("Install the "),r("a",{attrs:{href:"https://github.com/WP-API/Basic-Auth",target:"_blank",rel:"noopener noreferrer"}},[t._v("Basic Auth"),r("OutboundLink")],1),t._v(" plugin to allow authorization via HTTP Basic Auth.")])]),t._v(" "),r("h3",{attrs:{id:"user-accounts"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#user-accounts","aria-hidden":"true"}},[t._v("#")]),t._v(" User accounts")]),t._v(" "),r("p",[t._v("Sign in using the admin account and create the following user accounts:")]),t._v(" "),r("table",[r("thead",[r("tr",[r("th",[t._v("email")]),t._v(" "),r("th",[t._v("username")]),t._v(" "),r("th",[t._v("password")]),t._v(" "),r("th",[t._v("role")])])]),t._v(" "),r("tbody",[r("tr",[r("td",[t._v("admin@localhost.com")]),t._v(" "),r("td",[t._v("admin")]),t._v(" "),r("td",[t._v("admin")]),t._v(" "),r("td",[t._v("admin")])]),t._v(" "),r("tr",[r("td",[t._v("author@localhost.com")]),t._v(" "),r("td",[t._v("author")]),t._v(" "),r("td",[t._v("author")]),t._v(" "),r("td",[t._v("author")])]),t._v(" "),r("tr",[r("td",[t._v("employee@localhost.com")]),t._v(" "),r("td",[t._v("employee")]),t._v(" "),r("td",[t._v("employee")]),t._v(" "),r("td",[t._v("employee")])]),t._v(" "),r("tr",[r("td",[t._v("editor@localhost.com")]),t._v(" "),r("td",[t._v("editor")]),t._v(" "),r("td",[t._v("editor")]),t._v(" "),r("td",[t._v("editor")])]),t._v(" "),r("tr",[r("td",[t._v("subscriber@localhost.com")]),t._v(" "),r("td",[t._v("subscriber")]),t._v(" "),r("td",[t._v("subscriber")]),t._v(" "),r("td",[t._v("subscriber")])])])]),t._v(" "),r("h2",{attrs:{id:"input-symbols"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#input-symbols","aria-hidden":"true"}},[t._v("#")]),t._v(" Input symbols")]),t._v(" "),r("p",[t._v("The input alphabet consists of the following 11 symbols:")]),t._v(" "),r("ul",[r("li",[r("strong",[t._v("Create Post")]),t._v(" Creates a new article.")]),t._v(" "),r("li",[r("strong",[t._v("Delete Post")]),t._v(" Deletes the article.")]),t._v(" "),r("li",[r("strong",[t._v("Login {Admin, Author, Contributor, Editor, Subscriber}")]),t._v(" Sets the variable for which user to use for the requests.")]),t._v(" "),r("li",[r("strong",[t._v("Set Post {Draft, Future, Pending, Private, Publish}")]),t._v(" Sets the status of the article.")])]),t._v(" "),r("h2",{attrs:{id:"learning-setup"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#learning-setup","aria-hidden":"true"}},[t._v("#")]),t._v(" Learning setup")]),t._v(" "),r("p",[t._v("We learned the REST API with the following configuration:")]),t._v(" "),r("ul",[r("li",[r("em",[t._v("TTT")]),t._v(" algorithm")]),t._v(" "),r("li",[r("em",[t._v("Random word")]),t._v(" equivalence oracle (min=100, max=100, words=100, seed=42)")]),t._v(" "),r("li",[r("em",[t._v("HtmlUnit driver")]),t._v(" (although not required, it has to be specified)")])]),t._v(" "),r("p",[t._v("After about 23 minutes on an Intel Core i5-6600k, 16Gb RAM and an SSD, the learning process finished and produced an automaton model with 14 states.\nThe final model can be downloaded "),r("a",{attrs:{href:"./assets/model-wordpress-20181007.dot"}},[t._v("here")]),t._v(" and inspected in the web-based dot rendering tool "),r("a",{attrs:{href:"http://www.webgraphviz.com/",target:"_blank",rel:"noopener noreferrer"}},[t._v("WebGraphviz"),r("OutboundLink")],1),t._v(".\nThe model reveals several interesting properties.\nFor example the path (0 -> 7 -> 8 -> 9) shows that the status of an article that has been created by an admin can only be changed by the admin himself.")])])},[],!1,null,null,null);e.default=s.exports}}]);