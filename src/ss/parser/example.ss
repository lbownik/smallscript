# comment
:MyClass = Object subClass: "MyClass";

MyClass addField "value"; #another comment
MyClass addMethod: "method1" using: {:this :param1 |
     param1 > 0 ifTrue: 0 ifFalse: 1 ;
};

stdout print (MyClass new method1 3);

