# Frequently asked questions
  
**Can I learn any web application that is accessible over the internet?** <br> 
Yes, you can, but really should not do that, since testing usually takes a lot of traffic, and your test targets may not like it. 
But we do not programmatically prohibit it either. 
The primary use case is to use ALEX for testing applications that are installed locally or in the same network.
 
**Do I really not have to have any programming experience?** <br> 
We made best efforts to abstract all necessary steps to test a web application with ease. 
While modeling tests for your application, it may be helpful to have basic understanding of HTML and CSS. 
The same goes for modelling REST tests where it is required that you have basic understanding of the JSON format. 
 
**How potent does my system have to be to run tests?** <br> 
Learning is a CPU and memory intense process, but we give an lower bound for your system specs. 
From our own observations, it is advised to have at least 4Gb of RAM and a relatively modern Dual Core CPU to run tests at a satisfiable performance. 
The more power your system has, the faster your tests should (ideally) execute.
 
**I want to learn a specific feature of my application but ALEX does not provide a way to model it. What can I do?** <br> 
Initially, we did not develop ALEX as a solution to all situations that may occur in a web application, but to the most common ones.
If you think a key feature is missing, feel free to submit an issue or create a pull request on [GitHub][github] and we will see what we can do. 

[github]: https://github.com/LearnLib/alex