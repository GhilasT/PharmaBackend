import java.util.ArrayList;
import java.util.List;

public class PageDirectory // == HeaderPage 
{
    private static int numPages;
    // liste pour stocker les infos de chaque page(le pagid et son espace)
    public static List<PageInfo> pages;
                
    public PageDirectory() {
        this.numPages = 0;
        this.pages = new ArrayList<>();
    }

                
    // ajout d'une nouvelle page + son espace libre
    public static void addPage(PageID pageId, int espacelibe) {
       
        PageInfo p = new PageInfo(pageId, espacelibe);
        pages.add(p);
        numPages++;
    }

    // trouver une page avec un espace libre pour un nouvel enregistrement
    public static PageID findPageWithSpace(int sizeRecord) {
        for (PageInfo page : pages) {
            if (page.espacelibe >= sizeRecord) {
                
                return page.pageId;
            }
        }
        return null;
    }
}
