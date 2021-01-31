package leo.multithreadedfilecopier.dto;

/**
 * The possible types for elements that can be put to buffer. LETTER represents a
 * single character that has been read from source file. POISON_PILL represents an
 * element that signals for buffer readed that reading can end as all characters
 * have bene read from source file.
 * 
 * @author Leo Kallonen
 */
public enum MessageType {
    LETTER,
    POISON_PILL
}
