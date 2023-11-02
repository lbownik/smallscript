//------------------------------------------------------------------------------
//Copyright 2023 Lukasz Bownik
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//------------------------------------------------------------------------------
package ss.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
final class FastMap {

   /****************************************************************************
    * 
   ****************************************************************************/
   FastMap() {

      this.size = 0;
      this.table = new Node[initialCapacity];
      this.threshold = (int) (loadFactor * initialCapacity);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   FastMap(final FastMap other) {

      this.size = other.size;
      this.table = new Node[other.table.length];
      this.threshold = other.threshold;

      final int length = other.table.length;

      for (int index = 0; index < length; ++index) {
         final Node node = other.table[index];
         if (node != null) {
            this.table[index] = node.clone();
         }
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject get(final String key) {

      return getOrDefault(key, null);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   SSObject getOrDefault(final String key, final SSObject defaultValue) {

      final int hash = hash(key);
      Node node = this.table[(this.table.length - 1) & hash];

      while (node != null) {
         if (key.equals(node.key)) {
            return node.value;
         }
         node = node.next;
      }
      return defaultValue;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   void put(final String key, final SSObject value) {

      final int hash = hash(key);
      final Node[] tab = this.table;
      final int index = (tab.length - 1) & hash;
      Node node = tab[index];

      if (node == null) {
         tab[index] = new Node(key, value, null);
         ++this.size;
      } else {
         if (key.equals(node.key)) {
            node.value = value;
         } else {
            while (true) {
               if (node.next == null) {
                  node.next = new Node(key, value, null);
                  ++this.size;
                  break;
               } else {
                  final Node next = node.next;
                  if (key.equals(next.key)) {
                     next.value = value;
                     break;
                  }
                  node = next;
               }
            }
         }
      }
      if (this.size > this.threshold) {
         resize();
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private void resize() {

      final Node[] oldTable = this.table;
      final int oldLength = oldTable.length;
      final int newLength = oldLength * 2;

      if (oldLength >= maxXapacity) {
         this.threshold = Integer.MAX_VALUE;
      } else if (newLength < maxXapacity) {
         this.threshold *= 2;
      }

      this.table = new Node[newLength];
      copyNodes(oldTable, this.table);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   private static void copyNodes(final Node[] oldTab, final Node[] newTab) {

      final int oldLen = oldTab.length;
      final int newLen = newTab.length;

      for (int index = 0; index < oldLen; ++index) {
         Node node = oldTab[index];
         if (node != null) {
            oldTab[index] = null;
            if (node.next == null)
               newTab[hash(node.key) & (newLen - 1)] = node;
            else { // preserve order
               Node loHead = null;
               Node loTail = null;
               Node hiHead = null;
               Node hiTail = null;
               Node next;
               do {
                  next = node.next;
                  if ((hash(node.key) & oldLen) == 0) {
                     if (loTail == null)
                        loHead = node;
                     else
                        loTail.next = node;
                     loTail = node;
                  } else {
                     if (hiTail == null)
                        hiHead = node;
                     else
                        hiTail.next = node;
                     hiTail = node;
                  }
               } while ((node = next) != null);
               if (loTail != null) {
                  loTail.next = null;
                  newTab[index] = loHead;
               }
               if (hiTail != null) {
                  hiTail.next = null;
                  newTab[index + oldLen] = hiHead;
               }
            }
         }
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   void remove(final String key) {

      final int hash = hash(key);
      final int index = (this.table.length - 1) & hash;
      Node node = this.table[index];

      if (node != null) {
         if (node.key.equals(key)) {
            this.table[index] = node.next;
            --this.size;
            return;
         } else {
            for (Node next = node.next; next != null; node = next, next = next.next) {
               if (key.equals(node.key)) {
                  node.next = next.next;
                  --this.size;
                  return;
               }
            }
         }
      }
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   public Set<String> keySet() {

      final var result = new HashSet<String>();

      for (Node e : table) {
         for (; e != null; e = e.next) {
            result.add(e.key);
         }
      }

      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static final int hash(final String key) {

      final int h = key.hashCode();
      return h ^ (h >>> 16);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private Node[] table;
   private int threshold;
   private int size;

   private final static float loadFactor = 0.75f;
   private final static int initialCapacity = 32;
   private final static int maxXapacity = 1 << 30;

   /****************************************************************************
    * 
   ****************************************************************************/
   private static class Node {

      /*************************************************************************
       * 
      *************************************************************************/
      Node(final String key, final SSObject value, final Node next) {

         this.key = key;
         this.value = value;
         this.next = next;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      protected Node clone() {

         final Node node = new Node(this.key, this.value, null);
         if (this.next != null) {
            node.next = this.next.clone();
         }
         return node;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      final String key;
      SSObject value;
      Node next;
   }

   /****************************************************************************
    * 
   ****************************************************************************/

   public static void main(String[] args) throws Exception {

      testPutHashMap();
      testPutFastMap();

      testGetHashMap();
      testGetFasthMap();

   }

   static int iterations = 10000000;

   private static void testPutHashMap() {

      long begin = System.currentTimeMillis();

      for (int i = 0; i < iterations; ++i) {
         putIntoHashMap();
      }

      long duration = System.currentTimeMillis() - begin;

      System.out.println("putIntoHashMap: " + duration);
   }

   private static void testGetHashMap() {

      var map = putIntoHashMap();
      long begin = System.currentTimeMillis();

      for (int i = 0; i < iterations; ++i) {
         map.get("doesNotUnderstand:");
      }

      long duration = System.currentTimeMillis() - begin;

      System.out.println("testGetHashMap: " + duration);
   }

   private static void testPutFastMap() {

      long begin = System.currentTimeMillis();

      for (int i = 0; i < iterations; ++i) {
         putIntoFastMap();
      }

      long duration = System.currentTimeMillis() - begin;

      System.out.println("putIntoFastMap: " + duration);
   }

   private static void testGetFasthMap() {

      var map = putIntoFastMap();
      long begin = System.currentTimeMillis();

      for (int i = 0; i < iterations; ++i) {
         map.get("doesNotUnderstand:");
      }

      long duration = System.currentTimeMillis() - begin;

      System.out.println("testGetFastMap: " + duration);
   }

   private static HashMap<String, SSObject> putIntoHashMap() {

      var hashMap = new HashMap<String, SSObject>(32);

      hashMap.put("invoke::with:", SSNull.instance);
      hashMap.put("addField:", SSNull.instance);
      hashMap.put("addField::withValue:", SSNull.instance);
      hashMap.put("addImmutableField::withValue:", SSNull.instance);
      hashMap.put("addMethod::using:", SSNull.instance);
      hashMap.put("asString", SSNull.instance);
      hashMap.put("at:", SSNull.instance);
      hashMap.put("clone", SSNull.instance);
      hashMap.put("collectTo:", SSNull.instance);
      hashMap.put("doesNotUnderstand:", SSNull.instance);
      hashMap.put("equals:", SSNull.instance);
      hashMap.put("execute", SSNull.instance);
      hashMap.put("fields", SSNull.instance);
      hashMap.put("forEach:", SSNull.instance);
      hashMap.put("method:", SSNull.instance);
      hashMap.put("methods", SSNull.instance);
      hashMap.put("hash", SSNull.instance);
      hashMap.put("isNotEqualTo:", SSNull.instance);
      hashMap.put("orDefault:", SSNull.instance);
      hashMap.put("removeMethod:", SSNull.instance);
      hashMap.put("selectIf:", SSNull.instance);
      hashMap.put("throw", SSNull.instance);
      hashMap.put("transformUsing:", SSNull.instance);
      hashMap.put("try::catch:", SSNull.instance);

      return hashMap;
   }

   private static TreeMap<String, SSObject> putIntoTreeMap() {

      var hashMap = new TreeMap<String, SSObject>();

      hashMap.put("invoke::with:", SSNull.instance);
      hashMap.put("addField:", SSNull.instance);
      hashMap.put("addField::withValue:", SSNull.instance);
      hashMap.put("addImmutableField::withValue:", SSNull.instance);
      hashMap.put("addMethod::using:", SSNull.instance);
      hashMap.put("asString", SSNull.instance);
      hashMap.put("at:", SSNull.instance);
      hashMap.put("clone", SSNull.instance);
      hashMap.put("collectTo:", SSNull.instance);
      hashMap.put("doesNotUnderstand:", SSNull.instance);
      hashMap.put("equals:", SSNull.instance);
      hashMap.put("execute", SSNull.instance);
      hashMap.put("fields", SSNull.instance);
      hashMap.put("forEach:", SSNull.instance);
      hashMap.put("method:", SSNull.instance);
      hashMap.put("methods", SSNull.instance);
      hashMap.put("hash", SSNull.instance);
      hashMap.put("isNotEqualTo:", SSNull.instance);
      hashMap.put("orDefault:", SSNull.instance);
      hashMap.put("removeMethod:", SSNull.instance);
      hashMap.put("selectIf:", SSNull.instance);
      hashMap.put("throw", SSNull.instance);
      hashMap.put("transformUsing:", SSNull.instance);
      hashMap.put("try::catch:", SSNull.instance);

      return hashMap;
   }

   private static FastMap putIntoFastMap() {

      var hashMap = new FastMap();

      hashMap.put("invoke::with:", SSNull.instance);
      hashMap.put("addField:", SSNull.instance);
      hashMap.put("addField::withValue:", SSNull.instance);
      hashMap.put("addImmutableField::withValue:", SSNull.instance);
      hashMap.put("addMethod::using:", SSNull.instance);
      hashMap.put("asString", SSNull.instance);
      hashMap.put("at:", SSNull.instance);
      hashMap.put("clone", SSNull.instance);
      hashMap.put("collectTo:", SSNull.instance);
      hashMap.put("doesNotUnderstand:", SSNull.instance);
      hashMap.put("equals:", SSNull.instance);
      hashMap.put("execute", SSNull.instance);
      hashMap.put("fields", SSNull.instance);
      hashMap.put("forEach:", SSNull.instance);
      hashMap.put("method:", SSNull.instance);
      hashMap.put("methods", SSNull.instance);
      hashMap.put("hash", SSNull.instance);
      hashMap.put("isNotEqualTo:", SSNull.instance);
      hashMap.put("orDefault:", SSNull.instance);
      hashMap.put("removeMethod:", SSNull.instance);
      hashMap.put("selectIf:", SSNull.instance);
      hashMap.put("throw", SSNull.instance);
      hashMap.put("transformUsing:", SSNull.instance);
      hashMap.put("try::catch:", SSNull.instance);

      return hashMap;
   }

}
