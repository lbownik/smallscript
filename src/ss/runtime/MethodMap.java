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

import java.util.HashSet;
import java.util.Set;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
final class MethodMap implements Methods {

   /****************************************************************************
    * 
   ****************************************************************************/
   MethodMap() {

      this(Methods.empty);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   MethodMap(final Methods backup) {

      this.size = 0;
      this.table = new Node[initialCapacity];
      this.threshold = (int) (loadFactor * initialCapacity);
      this.backup = backup != null ? backup : Methods.empty;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   MethodMap(final MethodMap other) {

      this.size = other.size;
      this.table = new Node[other.table.length];
      this.threshold = other.threshold;
      this.backup = other.backup;

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
   @Override
   public SSObject get(final String key) {

      return getOrDefault(key, null);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject getOrDefault(final String key, final SSObject defaultValue) {

      Node node = this.table[(this.table.length - 1) & key.hashCode()];

      while (node != null) {
         if (key.equals(node.key)) {
            return node.value;
         }
         node = node.next;
      }
      return this.backup.getOrDefault(key, defaultValue);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public void add(final String key, final SSObject value) {

      final Node[] tab = this.table;
      final int index = (tab.length - 1) & key.hashCode();
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
               newTab[node.key.hashCode() & (newLen - 1)] = node;
            else { // preserve order
               Node loHead = null;
               Node loTail = null;
               Node hiHead = null;
               Node hiTail = null;
               Node next;
               do {
                  next = node.next;
                  if ((node.key.hashCode() & oldLen) == 0) {
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
   public Set<String> keySet() {

      final var result = new HashSet<String>();
      for (Node e : table) {
         for (; e != null; e = e.next) {
            result.add(e.key);
         }
      }
      result.addAll(this.backup.keySet());

      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private Node[] table;
   private int threshold;
   private int size;
   private final Methods backup;

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
      @Override
      public String toString() {

         return "Node [key=" + this.key + ", value=" + this.value + "]";
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
}
