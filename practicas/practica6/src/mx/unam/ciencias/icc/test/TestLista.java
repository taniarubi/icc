package mx.unam.ciencias.icc.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.icc.ExcepcionIndiceInvalido;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Lista}.
 */
public class TestLista {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* La lista. */
    private Lista<Integer> lista;

    /* Valida una lista. */
    private void validaLista(Lista<Integer> lista) {
        int longitud = lista.getLongitud();
        int[] arreglo = new int[longitud];
        int c = 0;
        Lista<Integer>.Nodo nodo = lista.getCabeza();
        while (nodo != null) {
            arreglo[c++] = nodo.get();
            nodo = nodo.getSiguiente();
        }
        Assert.assertTrue(c == longitud);
        c = 0;
        nodo = lista.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(arreglo[c++] == nodo.get());
            nodo = nodo.getSiguiente();
        }
        Assert.assertTrue(c == longitud);
        c = longitud - 1;
        nodo = lista.getRabo();
        while (nodo != null) {
            Assert.assertTrue(arreglo[c--] == nodo.get());
            nodo = nodo.getAnterior();
        }
    }

    /**
     * Crea un generador de números aleatorios para cada prueba, un número total
     * de elementos para nuestra lista, y una lista.
     */
    public TestLista() {
        random = new Random();
        total = 10 + random.nextInt(90);
        lista = new Lista<Integer>();
    }

    /**
     * Prueba unitaria para {@link Lista#Lista}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(lista != null);
        Assert.assertTrue(lista.esVacia());
        Assert.assertTrue(lista.getLongitud() == 0);
    }

    /**
     * Prueba unitaria para {@link Lista#getLongitud}.
     */
    @Test public void testGetLongitud() {
        Assert.assertTrue(lista.getLongitud() == 0);
        for (int i = 0; i < total/2; i++) {
            lista.agregaFinal(random.nextInt(total));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        for (int i = total/2; i < total; i++) {
            lista.agregaInicio(random.nextInt(total));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        Assert.assertTrue(lista.getLongitud() == total);
    }

    /**
     * Prueba unitaria para {@link Lista#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(lista.esVacia());
        lista.agregaFinal(1);
        Assert.assertFalse(lista.esVacia());
        lista.eliminaUltimo();
        Assert.assertTrue(lista.esVacia());
    }

    /**
     * Prueba unitaria para {@link Lista#agregaFinal}.
     */
    @Test public void testAgregaFinal() {
        validaLista(lista);
        try {
            lista.agregaFinal(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        lista.agregaFinal(1);
        Assert.assertTrue(lista.getUltimo() == 1);
        validaLista(lista);
        lista.agregaInicio(2);
        Assert.assertFalse(lista.getUltimo() == 2);
        validaLista(lista);
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            Assert.assertTrue(lista.getUltimo() == e);
            validaLista(lista);
        }
    }

    /**
     * Prueba unitaria para {@link Lista#agregaInicio}.
     */
    @Test public void testAgregaInicio() {
        validaLista(lista);
        try {
            lista.agregaInicio(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        lista.agregaInicio(1);
        Assert.assertTrue(lista.getPrimero() == 1);
        validaLista(lista);
        lista.agregaFinal(2);
        Assert.assertFalse(lista.getPrimero() == 2);
        validaLista(lista);
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            Assert.assertTrue(lista.getPrimero() == e);
            validaLista(lista);
        }
    }

    /**
     * Prueba unitaria para {@link Lista#inserta}.
     */
    @Test public void testInserta() {
        try {
            lista.inserta(0, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        Lista<Integer> otra = new Lista<Integer>();
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++) {
            int n = ini + i;
            otra.agregaInicio(n);
            lista.inserta(-1, n);
            Assert.assertTrue(lista.equals(otra));
            Assert.assertTrue(lista.getPrimero() == n);
            validaLista(lista);
        }
        lista = new Lista<Integer>();
        otra = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            int n = ini + i;
            otra.agregaInicio(n);
            lista.inserta(0, n);
            Assert.assertTrue(lista.equals(otra));
            Assert.assertTrue(lista.getPrimero() == n);
            validaLista(lista);
        }
        lista = new Lista<Integer>();
        otra = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            int n = ini + i;
            otra.agregaFinal(n);
            lista.inserta(lista.getLongitud(), n);
            Assert.assertTrue(lista.equals(otra));
            Assert.assertTrue(lista.getUltimo() == n);
            validaLista(lista);
        }
        for (int i = 0; i < total; i++) {
            int m = 1 + random.nextInt(total-2);
            lista = new Lista<Integer>();
            otra = new Lista<Integer>();
            for (int j = 0; j < total; j++) {
                int n = ini + j;
                otra.agregaFinal(n);
                if (j != m)
                    lista.agregaFinal(n);
                validaLista(lista);
            }
            Assert.assertTrue(otra.getLongitud() == lista.getLongitud() + 1);
            lista.inserta(m, ini + m);
            Assert.assertTrue(lista.equals(otra));
            validaLista(lista);
        }
    }

    /**
     * Prueba unitaria para {@link Lista#elimina}.
     */
    @Test public void testElimina() {
        lista.elimina(null);
        lista.elimina(0);
        lista.agregaFinal(1);
        Assert.assertFalse(lista.esVacia());
        lista.eliminaUltimo();
        Assert.assertTrue(lista.esVacia());
        int d = random.nextInt(total);
        int m = -1;
        for (int i = 0; i < total; i++) {
            lista.agregaInicio(d++);
            if (i == total / 2)
                m = d - 1;
        }
        int p = lista.getPrimero();
        int u = lista.getUltimo();
        Assert.assertTrue(lista.contiene(p));
        Assert.assertTrue(lista.contiene(m));
        Assert.assertTrue(lista.contiene(u));
        lista.elimina(p);
        Assert.assertFalse(lista.contiene(p));
        Assert.assertTrue(lista.getLongitud() == --total);
        validaLista(lista);
        lista.elimina(m);
        Assert.assertFalse(lista.contiene(m));
        Assert.assertTrue(lista.getLongitud() == --total);
        validaLista(lista);
        lista.elimina(u);
        Assert.assertFalse(lista.contiene(u));
        Assert.assertTrue(lista.getLongitud() == --total);
        validaLista(lista);
        while (!lista.esVacia()) {
            lista.elimina(lista.getPrimero());
            Assert.assertTrue(lista.getLongitud() == --total);
            validaLista(lista);
            if (lista.esVacia())
                continue;
            lista.elimina(lista.getUltimo());
            Assert.assertTrue(lista.getLongitud() == --total);
            validaLista(lista);
        }
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#eliminaPrimero}.
     */
    @Test public void testEliminaPrimero() {
        try {
            lista.eliminaPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int n = total;
        while (!lista.esVacia()) {
            Assert.assertTrue(n-- == lista.getLongitud());
            int k = lista.eliminaPrimero();
            Assert.assertTrue(k == a[i++]);
            validaLista(lista);
        }
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#eliminaUltimo}.
     */
    @Test public void testEliminaUltimo() {
        try {
            lista.eliminaUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int n = total;
        while (!lista.esVacia()) {
            Assert.assertTrue(n-- == lista.getLongitud());
            int k = lista.eliminaUltimo();
            Assert.assertTrue(k == a[total - ++i]);
            validaLista(lista);
        }
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#contiene}.
     */
    @Test public void testContiene() {
        Assert.assertFalse(lista.contiene(0));
        int d = random.nextInt(total);
        int m = -1;
        int n = d - 1;
        for (int i = 0; i < total; i++) {
            lista.agregaFinal(d++);
            if (i == total/2)
                m = d - 1;
        }
        Assert.assertTrue(lista.contiene(m));
        Assert.assertTrue(lista.contiene(new Integer(m)));
        Assert.assertFalse(lista.contiene(n));
    }

    /**
     * Prueba unitaria para {@link Lista#reversa}.
     */
    @Test public void testReversa() {
        Lista<Integer> reversa = lista.reversa();
        Assert.assertTrue(reversa.esVacia());
        Assert.assertFalse(reversa == lista);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        reversa = lista.reversa();
        Assert.assertFalse(lista == reversa);
        Assert.assertTrue(reversa.getLongitud() == lista.getLongitud());
        Lista.Nodo n1 = lista.getCabeza();
        Lista.Nodo n2 = reversa.getRabo();
        while (n1 != null && n2 != null) {
            Assert.assertTrue(n1.get().equals(n2.get()));
            n1 = n1.getSiguiente();
            n2 = n2.getAnterior();
        }
        Assert.assertTrue(n1 == null);
        Assert.assertTrue(n2 == null);
        validaLista(reversa);
    }

    /**
     * Prueba unitaria para {@link Lista#copia}.
     */
    @Test public void testCopia() {
        Lista<Integer> copia = lista.copia();
        Assert.assertTrue(copia.esVacia());
        Assert.assertFalse(copia == lista);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        copia = lista.copia();
        Assert.assertFalse(lista == copia);
        Assert.assertTrue(copia.getLongitud() == lista.getLongitud());
        Lista.Nodo n1 = lista.getCabeza();
        Lista.Nodo n2 = copia.getCabeza();
        while (n1 != null && n2 != null) {
            Assert.assertTrue(n1.get().equals(n2.get()));
            n1 = n1.getSiguiente();
            n2 = n2.getSiguiente();
        }
        Assert.assertTrue(n1 == null);
        Assert.assertTrue(n2 == null);
        validaLista(copia);
    }

    /**
     * Prueba unitaria para {@link Lista#limpia}.
     */
    @Test public void testLimpia() {
        int primero = random.nextInt(total);
        lista.agregaFinal(primero);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        int ultimo = random.nextInt(total);
        lista.agregaFinal(ultimo);
        Assert.assertFalse(lista.esVacia());
        Assert.assertTrue(primero == lista.getPrimero());
        Assert.assertTrue(ultimo == lista.getUltimo());
        Assert.assertFalse(lista.esVacia());
        Assert.assertFalse(lista.getLongitud() == 0);
        lista.limpia();
        Assert.assertTrue(lista.esVacia());
        Assert.assertTrue(lista.getLongitud() == 0);
        validaLista(lista);
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#getPrimero}.
     */
    @Test public void testGetPrimero() {
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            Assert.assertTrue(e == lista.getPrimero());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#getUltimo}.
     */
    @Test public void testGetUltimo() {
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            Assert.assertTrue(e == lista.getUltimo());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#get(int)}.
     */
    @Test public void testGet() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(lista.get(i) == a[i]);
        try {
            lista.get(-1);
            Assert.fail();
        } catch (ExcepcionIndiceInvalido eii) {}
        try {
            lista.get(total);
            Assert.fail();
        } catch (ExcepcionIndiceInvalido eii) {}
    }

    /**
     * Prueba unitaria para {@link Lista#indiceDe}.
     */
    @Test public void testIndiceDe() {
        Assert.assertTrue(lista.indiceDe(0) == -1);
        int ini = random.nextInt(total);
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i ++)
            Assert.assertTrue(i == lista.indiceDe(a[i]));
        Assert.assertTrue(lista.indiceDe(ini - 10) == -1);
    }

    /**
     * Prueba unitaria para {@link Lista#toString}.
     */
    @Test public void testToString() {
        Assert.assertTrue(lista.toString().equals("[]"));
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            lista.agregaFinal(a[i]);
        }
        String s = "[";
        for (int i = 0; i < total-1; i++)
            s += String.format("%d, ", a[i]);
        s += String.format("%d]", a[total-1]);
        Assert.assertTrue(s.equals(lista.toString()));
    }

    /**
     * Prueba unitaria para {@link Lista#equals}.
     */
    @Test public void testEquals() {
        Assert.assertFalse(lista.equals(null));
        Lista<Integer> otra = new Lista<Integer>();
        Assert.assertTrue(lista.equals(otra));
        for (int i = 0; i < total; i++) {
            int r = random.nextInt(total);
            lista.agregaFinal(r);
            otra.agregaFinal(new Integer(r));
        }
        Assert.assertTrue(lista.equals(otra));
        int u = lista.getUltimo();
        lista.elimina(u);
        Assert.assertFalse(lista.equals(otra));
        lista.agregaFinal(u + 1);
        Assert.assertFalse(lista.equals(otra));
        Assert.assertFalse(lista.equals(""));
        Assert.assertFalse(lista.equals(null));
    }

    /**
     * Prueba unitaria para {@link Lista#getCabeza}.
     */
    @Test public void testGetCabeza() {
        Assert.assertTrue(lista.getCabeza() == null);
        lista.agregaInicio(2);
        Assert.assertTrue(lista.getCabeza() != null);
        Assert.assertTrue(lista.getRabo() != null);
        Assert.assertTrue(lista.getCabeza().get().equals(2));
        lista.agregaInicio(1);
        Assert.assertTrue(lista.getCabeza() != null);
        Assert.assertTrue(lista.getCabeza().get().equals(1));
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            Assert.assertTrue(lista.getCabeza().get().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link Lista#getRabo}.
     */
    @Test public void testGetRabo() {
        Assert.assertTrue(lista.getRabo() == null);
        lista.agregaFinal(1);
        Assert.assertTrue(lista.getCabeza() != null);
        Assert.assertTrue(lista.getRabo() != null);
        Assert.assertTrue(lista.getRabo().get().equals(1));
        lista.agregaFinal(2);
        Assert.assertTrue(lista.getRabo() != null);
        Assert.assertTrue(lista.getRabo().get().equals(2));
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            Assert.assertTrue(lista.getRabo().get().equals(e));
        }
    }
}
