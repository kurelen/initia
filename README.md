# 📜 Initia

[![Tests](https://github.com/kurelen/initia/workflows/Run%20tests/badge.svg)](https://github.com/kurelen/initia/actions)
[![Documentation](https://github.com/kurelen/initia/workflows/Build%20and%20Deploy%20Docs/badge.svg)](https://kurelen.github.io/initia/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Clojure](https://img.shields.io/badge/Clojure-1.12.1-blue.svg)](https://clojure.org/)
[![Digital Humanities](https://img.shields.io/badge/Digital-Humanities-purple.svg)](#)

Initia ist ein Forschungswerkzeug zur computergestützten Analyse 
mittelalterlicher Textanfänge (Initien). Das Projekt nutzt Textmetriken 
und untersucht speziell den gewichteten Levenshtein Algorithmus, mit 
angepasster Gewichtung für mittelalterliche deutsche Schreibsprachen. 
Diese Metrik wird mit generischen Metriken anhand einer Beispielmenge an 
Gruppen von Initien verglichen.

## 👥 Mitwirkende

- **Nicole Eichenberger** (Staatsbibliothek zu Berlin - Stiftung Preußischer Kulturbesitz)
- **Magdalena Luniak** (Staatsbibliothek zu Berlin - Stiftung Preußischer Kulturbesitz)
- **Alexander Jandt** (Staatsbibliothek zu Berlin - Stiftung Preußischer Kulturbesitz)

## 📊 Dokumentation der Ergebnisse

Die Ergebnisse der Analyse einer Beispielmenge an Initien sind unter 
[kurelen.github.io/initia](https://kurelen.github.io/initia/) 
veröffentlicht. Diese interaktiven Notebooks demonstrieren die 
verschiedenen Analysemethoden und visualisieren die Resultate.

Die Dokumentation wurde mit [Clerk](https://github.com/nextjournal/clerk) 
erstellt und ermöglicht die Reproduktion aller Analyseschritte.

## 🛠️ Technologie

Das Projekt basiert auf [Clojure](https://clojure.org/) und nutzt folgende 
Bibliotheken:

- **[java-string-similarity](https://github.com/tdebatty/java-string-similarity)** 
  Verschiedene String-Ähnlichkeitsalgorithmen
- **[core.matrix](https://github.com/mikera/core.matrix)** 
  Matrix-Operationen und numerische Berechnungen
- **[vectorz-clj](https://github.com/mikera/vectorz-clj)** 
  Vektor-Implementierung
- **[tools.cli](https://github.com/clojure/tools.cli)** 
  Kommandozeilen-Interface
- **[Clerk](https://github.com/nextjournal/clerk)** 
  Notebook-basierte Dokumentation

## 🚀 Installation & Verwendung

### Voraussetzungen
- Java 21 oder höher
- [Clojure CLI](https://clojure.org/guides/install_clojure)

### Lokale Entwicklung
```bash
git clone https://github.com/kurelen/initia.git
cd initia
make help  # Zeigt verfügbare Kommandos
```

### CLI-Tool verwenden

Das CLI-Tool berechnet eine Matrix aller paarweisen Text-Ähnlichkeiten/-Distanzen für eine Liste von Initien.

**Eingabe der Initien:**
- **Interaktiv:** Initien direkt in der Kommandozeile eingeben (eine Leerzeile beendet die Eingabe)
- **Datei:** Initien aus einer Textdatei laden über die Option `-i`, wobei jede Zeile einem Initium entspricht

**Ausgabe:**
- Standardmäßig wird das Ergebnis in der Kommandozeile angezeigt
- Mit der Option `-o` kann das Ergebnis in eine Datei geschrieben werden

```bash
Usage: clj -M:run [options]
   or: java -jar initia.jar [options]

Options:
  -m, --metric METRIC  :medieval-sim  Similarity metric to use
  -i, --input FILE                    Input file with initia
  -o, --output FILE                   Output file for results
  -v, --verbose                       Verbose output
  -h, --help                          Show help

Available metrics:
  damerau-dist - Damerau distance
  jaccard-dist - Jaccard distance
  jaccard-sim - Jaccard similarity
  cosine-dist - Cosine distance
  jaro-winkler-dist - Jaro-Winkler distance
  lcs-dist - LCS distance
  medieval-dist - Medieval distance (weighted levenshtein)
  lcs-sim - LCS similarity
  cosine-sim - Cosine similarity
  jaro-winkler-sim - Jaro-Winkler similarity
  levenshtein-sim - Levenshtein similarity
  levenshtein-dist - Levenshtein distance
  medieval-sim - Medieval similarity (weighted levenshtein)
  damerau-sim - Damerau similarity
  ngram-sim - N-Gram similarity
```

### Notebooks lokal ansehen
```bash
make serve
# Öffne http://localhost:7777 im Browser
```

### Entwicklung
```bash
make test    # Tests ausführen
make lint    # Code-Qualität prüfen  
make format  # Code formatieren
```

## 📄 Lizenz

Dieses Projekt steht unter der [MIT License](LICENSE).

