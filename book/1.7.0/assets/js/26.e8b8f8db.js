(window.webpackJsonp=window.webpackJsonp||[]).push([[26],{293:function(e,t,a){"use strict";a.r(t);var n=a(0),r=Object(n.a)({},function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("ContentSlotsDistributor",{attrs:{"slot-key":e.$parent.slotKey}},[a("h1",{attrs:{id:"installation"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#installation","aria-hidden":"true"}},[e._v("#")]),e._v(" Installation")]),e._v(" "),a("p",[e._v("In order to use ALEX on your system, make sure that "),a("strong",[e._v("Java 8")]),e._v(" is installed.\nFurther, a "),a("strong",[e._v("modern browser")]),e._v(" like Google Chrome, Mozilla Firefox or Microsoft Edge with JavaScript enabled is required to use the web interface.")]),e._v(" "),a("h2",{attrs:{id:"bundled-version"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#bundled-version","aria-hidden":"true"}},[e._v("#")]),e._v(" Bundled version")]),e._v(" "),a("ol",[a("li",[a("a",{attrs:{href:"https://github.com/LearnLib/alex/releases/download/v1.7.0/alex-1.7.0.war",target:"_blank",rel:"noopener noreferrer"}},[e._v("Download"),a("OutboundLink")],1),e._v(" the latest version.")]),e._v(" "),a("li",[e._v("Open a terminal and start ALEX via "),a("code",[e._v("java -jar alex-1.7.0.war")]),e._v(".")]),e._v(" "),a("li",[e._v("Wait until the command line prints something like "),a("code",[e._v("Started App in XX.XXX seconds")]),e._v(".")]),e._v(" "),a("li",[e._v("Open "),a("em",[e._v("http://localhost:8000")]),e._v(" in a web browser.")])]),e._v(" "),a("p",[e._v("After the first start, you can login as an admin using the account below:")]),e._v(" "),a("p",[e._v("Email: "),a("em",[e._v("admin@alex.example")]),e._v(" "),a("br"),e._v("\nPassword: "),a("em",[e._v("admin")])]),e._v(" "),a("h2",{attrs:{id:"from-source"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#from-source","aria-hidden":"true"}},[e._v("#")]),e._v(" From source")]),e._v(" "),a("p",[e._v("In order to build ALEX from source, make sure that you have the following software installed:")]),e._v(" "),a("ul",[a("li",[e._v("Java JDK 8")]),e._v(" "),a("li",[e._v("Maven 3")]),e._v(" "),a("li",[e._v("Node.js (v10.0.0) and the NPM (v6.0.0)")])]),e._v(" "),a("p",[e._v("To build ALEX, open a terminal and follow the instructions below:")]),e._v(" "),a("div",{staticClass:"language-bash line-numbers-mode"},[a("pre",{pre:!0,attrs:{class:"language-bash"}},[a("code",[a("span",{pre:!0,attrs:{class:"token comment"}},[e._v("# clone the repository")]),e._v("\n"),a("span",{pre:!0,attrs:{class:"token function"}},[e._v("git")]),e._v(" clone https://github.com/LearnLib/alex.git\n\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[e._v("# navigate to the project directory")]),e._v("\n"),a("span",{pre:!0,attrs:{class:"token function"}},[e._v("cd")]),e._v(" alex\n\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[e._v("# build ALEX")]),e._v("\nmvn "),a("span",{pre:!0,attrs:{class:"token function"}},[e._v("install")]),e._v(" package "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[e._v("[")]),e._v("-DskipTests"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[e._v("]")]),e._v("\n")])]),e._v(" "),a("div",{staticClass:"line-numbers-wrapper"},[a("span",{staticClass:"line-number"},[e._v("1")]),a("br"),a("span",{staticClass:"line-number"},[e._v("2")]),a("br"),a("span",{staticClass:"line-number"},[e._v("3")]),a("br"),a("span",{staticClass:"line-number"},[e._v("4")]),a("br"),a("span",{staticClass:"line-number"},[e._v("5")]),a("br"),a("span",{staticClass:"line-number"},[e._v("6")]),a("br"),a("span",{staticClass:"line-number"},[e._v("7")]),a("br"),a("span",{staticClass:"line-number"},[e._v("8")]),a("br")])]),a("p",[e._v("The bundle can then be found at "),a("code",[e._v("build/target/alex-build-1.7.0.war")]),e._v(".\nRun it using the instructions for running the bundled version from above.")]),e._v(" "),a("h2",{attrs:{id:"docker"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#docker","aria-hidden":"true"}},[e._v("#")]),e._v(" Docker")]),e._v(" "),a("p",[e._v("We also offer a Docker image that contains a Linux environment with the following software:")]),e._v(" "),a("ul",[a("li",[e._v("ALEX v1.7.0")]),e._v(" "),a("li",[e._v("Chrome v73")]),e._v(" "),a("li",[e._v("Firefox v66")])]),e._v(" "),a("p",[e._v("The image can be found "),a("a",{attrs:{href:"docker"}},[e._v("here")]),e._v(".\nThe Docker container exposes the "),a("strong",[e._v("port 8000")]),e._v(" under which ALEX is available after the start.\nNote that currently, only the HtmlUnit and the Firefox browser can be used in the Docker container.")])])},[],!1,null,null,null);t.default=r.exports}}]);