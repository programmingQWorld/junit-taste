# Summary of Changes between 1.0 and 2.1


## We renamed the package prefix for all junit classes from "test" to "junit."

## Several new assert methods were added:

## Exceptions during setUp() and tearDown() are now caught and reported.

## A warning is now given when a TestCase class isn't public or has no test methods.

## All the assert methods are now public.

## Both the batch and the interactive TestRunner no longer require that the Test class provides a static suite() method.There is a new variation of the graphical TestRunner (junit.ui.TestRunner) the LoadingTestRunner.
    
（批处理和交互式TestRunner都不再需要Test类提供静态suite（）方法。TestRunner（junit.ui.TestRunner）有一个新的变体，即LoadingTestRunner。）
