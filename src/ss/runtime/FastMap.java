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
final class FastMap {

   /****************************************************************************
    * 
   ****************************************************************************/
   public FastMap() {

      this.size = 0;
      this.table = new Node[initialCapacity];
      this.threshold = (int) (loadFactor * initialCapacity);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public FastMap(final FastMap other) {

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
   public SSObject get(final String key) {

      return getOrDefault(key, null);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject getOrDefault(final String key, final SSObject defaultValue) {

      final int hash = hash(key);
      Node node = this.table[(this.table.length - 1) & hash];

      while (node != null) {
         // if (first.hash == hash) {
         if (key.equals(node.key)) {
            return node.value;
         }
         //}
         node = node.next;
      }
      return defaultValue;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public void put(final String key, final SSObject value) {

      int hash = hash(key);
      Node[] tab = this.table;

      int n;

      if (tab == null || (n = tab.length) == 0) {
         tab = resize();
         n = tab.length;
      }

      Node p;
      int i;
      if ((p = tab[i = (n - 1) & hash]) == null)
         tab[i] = new Node(hash, key, value, null);
      else {
         Node e;
         String k;

         if (p.hash == hash
               && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
         else {
            for (int binCount = 0;; ++binCount) {
               if ((e = p.next) == null) {
                  p.next = new Node(hash, key, value, null);
                  break;
               }
               if (e.hash == hash
                     && ((k = e.key) == key || (key != null && key.equals(k))))
                  break;
               p = e;
            }
         }
         if (e != null) { // existing mapping for key
            e.value = value;
         }
      }
      if (++this.size > this.threshold) {
         resize();
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/

   /**
    * Initializes or doubles table size. If null, allocates in accord with initial
    * capacity target held in field threshold. Otherwise, because we are using
    * power-of-two expansion, the elements from each bin must either stay at same
    * index, or move with a power of two offset in the new table.
    *
    * @return the table
    */
   final Node[] resize() {

      Node[] oldTab = this.table;
      int oldCap = (oldTab == null) ? 0 : oldTab.length;
      int oldThr = this.threshold;
      int newCap, newThr = 0;

      if (oldCap > 0) {
         if (oldCap >= maxXapacity) {
            this.threshold = Integer.MAX_VALUE;
            return oldTab;
         } else if ((newCap = oldCap << 1) < maxXapacity
               && oldCap >= initialCapacity)
            newThr = oldThr << 1; // double threshold
      } else if (oldThr > 0) // initial capacity was placed in threshold
         newCap = oldThr;
      else { // zero initial threshold signifies using defaults
         newCap = initialCapacity;
         newThr = (int) (loadFactor * initialCapacity);
      }

      if (newThr == 0) {
         float ft = (float) newCap * loadFactor;
         newThr = (newCap < maxXapacity && ft < (float) maxXapacity ? (int) ft
               : Integer.MAX_VALUE);
      }

      this.threshold = newThr;
      Node[] newTab = (Node[]) new Node[newCap];
      this.table = newTab;
      if (oldTab != null) {
         copyNodes(oldTab, oldCap, newCap, newTab);
      }
      return newTab;
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   private void copyNodes(Node[] oldTab, int oldCap, int newCap, Node[] newTab) {

      for (int j = 0; j < oldCap; ++j) {
         Node e;
         if ((e = oldTab[j]) != null) {
            oldTab[j] = null;
            if (e.next == null)
               newTab[e.hash & (newCap - 1)] = e;
            else { // preserve order
               Node loHead = null, loTail = null;
               Node hiHead = null, hiTail = null;
               Node next;
               do {
                  next = e.next;
                  if ((e.hash & oldCap) == 0) {
                     if (loTail == null)
                        loHead = e;
                     else
                        loTail.next = e;
                     loTail = e;
                  } else {
                     if (hiTail == null)
                        hiHead = e;
                     else
                        hiTail.next = e;
                     hiTail = e;
                  }
               } while ((e = next) != null);
               if (loTail != null) {
                  loTail.next = null;
                  newTab[j] = loHead;
               }
               if (hiTail != null) {
                  hiTail.next = null;
                  newTab[j + oldCap] = hiHead;
               }
            }
         }
      }
   }

   /**
    * Removes the mapping for the specified key from this map if present.
    *
    * @param key key whose mapping is to be removed from the map
    * @return the previous value associated with {@code key}, or {@code null} if
    *         there was no mapping for {@code key}. (A {@code null} return can also
    *         indicate that the map previously associated {@code null} with
    *         {@code key}.)
    */
   public SSObject remove(String key) {
      Node e;
      return (e = removeNode(hash(key), key, null, false, true)) == null ? null
            : e.value;
   }

   /**
    * Implements Map.remove and related methods.
    *
    * @param hash       hash for key
    * @param key        the key
    * @param value      the value to match if matchValue, else ignored
    * @param matchValue if true only remove if value is equal
    * @param movable    if false do not move other nodes while removing
    * @return the node, or null if none
    */
   final Node removeNode(int hash, Object key, Object value, boolean matchValue,
         boolean movable) {
      Node[] tab;
      Node p;
      int n, index;
      if ((tab = table) != null && (n = tab.length) > 0
            && (p = tab[index = (n - 1) & hash]) != null) {
         Node node = null, e;
         String k;
         SSObject v;
         if (p.hash == hash
               && ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
         else if ((e = p.next) != null) {
            do {
               if (e.hash == hash
                     && ((k = e.key) == key || (key != null && key.equals(k)))) {
                  node = e;
                  break;
               }
               p = e;
            } while ((e = e.next) != null);

         }
         if (node != null && (!matchValue || (v = node.value) == value
               || (value != null && value.equals(v)))) {
            if (node == p)
               tab[index] = node.next;
            else
               p.next = node.next;
            --size;
            return node;
         }
      }
      return null;
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
   static final int hash(final String key) {

      final int h = key.hashCode();
      return h ^ (h >>> 16);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static final int tableSizeFor(int cap) {

      int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
      return (n < 0) ? 1 : (n >= maxXapacity) ? maxXapacity : n + 1;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private Node[] table;
   private int threshold;
   private int size;

   private final static float loadFactor = 0.75f;
   private final static int initialCapacity = 16;
   private final static int maxXapacity = 1 << 30;

   /****************************************************************************
    * 
   ****************************************************************************/
   private static class Node {

      /*************************************************************************
       * 
      *************************************************************************/
      Node(final int hash, final String key, final SSObject value, final Node next) {

         this.hash = hash;
         this.key = key;
         this.value = value;
         this.next = next;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      protected Node clone() {

         final Node node = new Node(this.hash, this.key, this.value, null);
         if (this.next != null) {
            node.next = this.next.clone();
         }
         return node;
      }

      /*************************************************************************
       * 
      *************************************************************************/
      public final String toString() {

         return key + "=" + value;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      public final boolean equals(final Object o) {
         if (o == this) {
            System.err.println("Node equals this");
            return true;
         } else {
            final Node n = (Node) o;
            return this.key.equals(n.key) && this.value.equals(n.value);
         }
      }
      /*************************************************************************
       * 
      *************************************************************************/
      final int hash;
      final String key;
      SSObject value;
      Node next;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
}
