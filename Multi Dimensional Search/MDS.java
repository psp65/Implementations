package psp170230;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class to perform Multidimensional Search on given dataset with 3 attributes
 *
 * Multi-dimensional search: Consider the web site of a seller like Amazon. They
 * carry tens of thousands of products, and each product has many attributes
 * (Name, Size, Description, Keywords, Manufacturer, Price, etc.). The search
 * engine allows users to specify attributes of products that they are seeking,
 * and shows products that have most of those attributes. To make search
 * efficient, the data is organized using appropriate data structures, such as
 * balanced trees. But, if products are organized by Name, how can search by
 * price implemented efficiently? The solution, called indexing in databases, is
 * to create a new set of references to the objects for each search field, and
 * organize them to implement search operations on that field efficiently.
 *
 *
 * @author Param Parikh, Tej Patel
 */
public class MDS {

    /**
     * Treeset to store ids
     */
    private final TreeSet<Long> ids;

    /**
     * Map to store id as key and price as value
     */
    private final HashMap<Long, Money> idToPrice;

    /**
     * Map to store id as key and description as value
     */
    private final HashMap<Long, Set<Long>> idToDesc;

    /**
     * Map to store description as key and set of ids as values
     */
    private final HashMap<Long, Set<Long>> descToId;

    /**
     * Initializes the object properties
     */
    public MDS() {
        ids = new TreeSet<>();
        idToDesc = new HashMap<>();
        idToPrice = new HashMap<>();
        descToId = new HashMap<>();
    }

    /**
     * Insert a new item whose description is given in the list. If an entry
     * with the same id already exists, then its description and price are
     * replaced by the new values, unless list is null or empty, in which case,
     * just the price is updated. Returns 1 if the item is new, and 0 otherwise.
     *
     * @param id Id of the item
     * @param price Price of item
     * @param list List of descriptions
     * @return 1 if item is successfully inserted else 0
     */
    public int insert(long id, Money price, java.util.List<Long> list) {
        if (!idToPrice.containsKey(id)) {
            ids.add(id);
            idToPrice.put(id, price);

            Set<Long> s = new HashSet<>(list);
            idToDesc.put(id, s);

            for (Long d : list) {
                if (!descToId.containsKey(d)) {
                    Set<Long> newList = new HashSet<>();
                    newList.add(id);
                    descToId.put(d, newList);
                } else {
                    descToId.get(d).add(id);
                }
            }
            return 1;
        }

        if (list == null || list.isEmpty()) {
            idToPrice.put(id, price);
            return 0;
        }

        delete(id);
        insert(id, price, list);
        return 0;
    }

    /**
     * Return price of item with given id (or 0, if not found).
     *
     * @param id Id of item
     * @return Price in form of Money object
     */
    public Money find(long id) {
        if (idToPrice.containsKey(id)) {
            return idToPrice.get(id);
        }
        return new Money();
    }

    /**
     * Delete item from storage. Returns the sum of the long int's that are in
     * the description of the item deleted (or 0, if such an id did not exist).
     *
     * @param id Id of item
     * @return Sum of descriptions of deleted item
     */
    public long delete(long id) {
        long sum = 0;

        if (idToPrice.containsKey(id)) {
            ids.remove(id);
            idToPrice.remove(id);

            for (Long d : idToDesc.get(id)) {
                descToId.get(d).remove(id);
                sum += d;
            }

            idToDesc.remove(id);
        }
        return sum;
    }

    /**
     * Given a long int, find items whose description contains that number
     * (exact match with one of the long int's in the item's description), and
     * return lowest price of those items. Return 0 if there is no such item.
     *
     * @param n Description of item
     * @return Minimum price of the item
     */
    public Money findMinPrice(long n) {

        if (descToId.containsKey(n)) {
            Money min = new Money(Long.MAX_VALUE, 99);

            Iterator<Long> it = descToId.get(n).iterator();
            while (it.hasNext()) {
                Long id = it.next();
                if (idToPrice.get(id).compareTo(min) < 0) {
                    min = idToPrice.get(id);
                }
            }

            if (min.dollars() == Long.MAX_VALUE && min.cents() == 99) {
                return new Money();
            }

            return min;
        }

        return new Money();
    }

    /**
     * Given a long int, find items whose description contains that number, and
     * return highest price of those items. Return 0 if there is no such item.
     *
     * @param n Description of item
     * @return Maximum price of the item
     */
    public Money findMaxPrice(long n) {

        if (descToId.containsKey(n)) {
            Money max = new Money();

            Iterator<Long> it = descToId.get(n).iterator();
            while (it.hasNext()) {
                Long id = it.next();
                if (idToPrice.get(id).compareTo(max) > 0) {
                    max = idToPrice.get(id);
                }
            }
            return max;
        }

        return new Money();
    }

    /**
     * Given a long int n, find the number of items whose description contains
     * n, and in addition, their prices fall within the given range, [low,
     * high].
     *
     * @param n Description number
     * @param low Low price
     * @param high High Price
     * @return Total items
     */
    public int findPriceRange(long n, Money low, Money high) {
        int items = 0;

        if (!descToId.containsKey(n)) {
            return 0;
        }

        Iterator<Long> it = descToId.get(n).iterator();
        while (it.hasNext()) {
            long id = it.next();
            if (idToPrice.get(id).compareTo(low) >= 0 && idToPrice.get(id).compareTo(high) <= 0) {
                items++;
            }
        }

        return items;
    }

    /**
     * Increase the price of every product, whose id is in the range [l,h] by
     * r%. Discard any fractional pennies in the new prices of items. Note that
     * you are truncating, not rounding. Returns the sum of the net increases of
     * the prices.
     *
     * @param l low range of id
     * @param h high range of id
     * @param rate amount by which price is to be increased
     * @return Total increase in price
     */
    public Money priceHike(long l, long h, double rate) {

        NavigableSet<Long> range = ids.subSet(l, true, h, true);
        Iterator<Long> it = range.iterator();
        BigDecimal big_totalHike = new BigDecimal("0");
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal big_rate = new BigDecimal(Double.toString(rate));
        //long totalHike = 0;

        while (it.hasNext()) {
            Long id = it.next();
            Money oldMoney = idToPrice.get(id);

            BigDecimal big_oldPrice;
            if (oldMoney.cents() > 0 && oldMoney.cents() < 10) {
                big_oldPrice = new BigDecimal(oldMoney.dollars() + ".0" + oldMoney.cents());
            } else {
                big_oldPrice = new BigDecimal(oldMoney.dollars() + "." + oldMoney.cents());
            }

            BigDecimal big_newPrice = big_oldPrice.add(big_oldPrice.multiply(big_rate).divide(hundred)).setScale(2, RoundingMode.DOWN);
            idToPrice.put(id, new Money(big_newPrice.toString()));

            big_totalHike = big_totalHike.add(big_newPrice.subtract(big_oldPrice)).setScale(2, RoundingMode.DOWN);

            /*
            // Not highly precise but comparatively speedy compared to BigDecimal
            Money oldMoney = idToPrice.get(id);
            long oldPrice = (oldMoney.dollars() * 100) + oldMoney.cents();
            long newPrice = (long) Math.floor((oldPrice + ((oldPrice * rate) / 100)));
            
            Money newMoney = new Money(newPrice / 100, (int) newPrice % 100);
            idToPrice.put(id, newMoney);

            totalHike += (newPrice - oldPrice);
             */
        }
        return new Money(big_totalHike.toString());
        //return new Money(totalHike / 100, (int) totalHike % 100);
    }

    /**
     * Removes elements of list from the description of id. It is possible that
     * some of the items in the list are not in the id's description. Returns
     * the sum of the numbers that are actually deleted from the description of
     * id. Return 0 if there is no such id.
     *
     * @param id id from which descriptions needs to be removed
     * @param list descriptions which needs to be removed, if present in id
     * @return sum of deleted descriptions from id
     */
    public long removeNames(long id, java.util.List<Long> list) {
        long sum = 0;

        if (!idToDesc.containsKey(id)) {
            return 0;
        }

        Set<Long> description = idToDesc.get(id);

        for (Long desc : list) {
            if (description.contains(desc)) {
                description.remove(desc);
                descToId.get(desc).remove(id);
                sum += desc;
            }
        }

        idToDesc.put(id, description);
        return sum;
    }

    /**
     * Class to store Price attached with each id. Price is separated in Dollars
     * and Cents.
     *
     */
    public static class Money implements Comparable<Money> {

        long d;
        int c;

        public Money() {
            d = 0;
            c = 0;
        }

        public Money(long d, int c) {
            this.d = d;
            this.c = c;
        }

        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if (len < 1) {
                d = 0;
                c = 0;
            } else if (part.length == 1) {
                d = Long.parseLong(s);
                c = 0;
            } else {
                d = Long.parseLong(part[0]);
                c = Integer.parseInt(part[1]);
            }
        }

        public long dollars() {
            return d;
        }

        public int cents() {
            return c;
        }

        @Override
        public int compareTo(Money other) {

            if (this.d > other.d) {
                return 1;
            }

            if (this.d < other.d) {
                return -1;
            }

            if (this.c > other.c) {
                return 1;
            }

            if (this.c < other.c) {
                return -1;
            }

            return 0;
        }

        @Override
        public String toString() {
            return d + "." + c;
        }
    }
}
