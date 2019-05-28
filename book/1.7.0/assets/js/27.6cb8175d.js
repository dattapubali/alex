(window.webpackJsonp=window.webpackJsonp||[]).push([[27],{313:function(e,t,i){"use strict";i.r(t);var a=i(0),n=Object(a.a)({},function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("ContentSlotsDistributor",{attrs:{"slot-key":e.$parent.slotKey}},[i("h1",{attrs:{id:"model-checking"}},[i("a",{staticClass:"header-anchor",attrs:{href:"#model-checking","aria-hidden":"true"}},[e._v("#")]),e._v(" Model checking")]),e._v(" "),i("p",[e._v("ALEX allows you to automatically verify properties of learned models using model checking.\nTherefor, it offers the possibility to define LTL formulas.")]),e._v(" "),i("h2",{attrs:{id:"setting-up-ltsmin"}},[i("a",{staticClass:"header-anchor",attrs:{href:"#setting-up-ltsmin","aria-hidden":"true"}},[e._v("#")]),e._v(" Setting up LTSmin")]),e._v(" "),i("p",[e._v("Model checking abilities in ALEX depend on the model checking library "),i("a",{attrs:{href:"ltsmin"}},[e._v("LTSmin")]),e._v(".\nThe library has to be downloaded from the homepage and extracted to a place of your choice.\nInside the folder, there is a "),i("em",[e._v("bin")]),e._v(" directory that contains all the binaries.\nWhen starting ALEX, specify the path to the bin directory as an additional argument:")]),e._v(" "),i("p",[i("code",[e._v("java -jar alex-1.7.0.war --ltsmin.path=/path/to/ltsmin/bin")])]),e._v(" "),i("h2",{attrs:{id:"define-ltl-formulas"}},[i("a",{staticClass:"header-anchor",attrs:{href:"#define-ltl-formulas","aria-hidden":"true"}},[e._v("#")]),e._v(" Define LTL formulas")]),e._v(" "),i("p",[e._v("Currently, ALEX only supports model checking using LTL formulas.\nTo define formulas that you want to have checked later on:")]),e._v(" "),i("ol",[i("li",[i("p",[e._v("In the sidebar, click on "),i("strong",[e._v("Learning > Lts Formulas")])])]),e._v(" "),i("li",[i("p",[e._v("Click on the "),i("strong",[e._v("Create")]),e._v("-button in the action bar")])]),e._v(" "),i("li",[i("p",[e._v("In the dialog, define a property using "),i("a",{attrs:{href:"ltl-syntax"}},[e._v("this syntax")]),e._v(" and give the formula an optional name.\nAdditionally, use the keywords "),i("em",[e._v("input")]),e._v(" and "),i("em",[e._v("output")]),e._v(" to query edge labels of the automaton, e.g.")]),e._v(" "),i("p",[i("code",[e._v('[](input == "Delete" -> output == "Ok")')])])]),e._v(" "),i("li",[i("p",[e._v("Click on "),i("strong",[e._v("Create")])])])]),e._v(" "),i("h2",{attrs:{id:"verifying-properties"}},[i("a",{staticClass:"header-anchor",attrs:{href:"#verifying-properties","aria-hidden":"true"}},[e._v("#")]),e._v(" Verifying properties")]),e._v(" "),i("ol",[i("li",[e._v("Open any learned model")]),e._v(" "),i("li",[e._v("Select the model checking view by selecting the item "),i("strong",[e._v("Checking")]),e._v(" from the select menu on the top right")]),e._v(" "),i("li",[e._v("Select all formulas that you want to have verified or enter additional properties")]),e._v(" "),i("li",[e._v("Optionally, change the parameters for minimum unfolds and the multiplier")]),e._v(" "),i("li",[e._v("Click on the "),i("strong",[e._v("Run checks")]),e._v("-button")]),e._v(" "),i("li",[e._v("If a counterexample could be found, it is displayed below the corresponding formula")])])])},[],!1,null,null,null);t.default=n.exports}}]);