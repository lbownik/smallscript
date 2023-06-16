# comment
:MyClass = Object subClass: "MyClass";

MyClass addField "value"; #another comment
MYClass addMethod: "method1" using: {:self :param1 |
     param1 > 0 ifTrue: 0 fFalse: 1 ;
};

stdout print (MyClass method1 3);

