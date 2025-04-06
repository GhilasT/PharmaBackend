public class RecordId {
	private PageID pageId;
	private int slotIdx;

	public RecordId(PageID pageId, int slotIdx) {
		this.pageId = pageId;
		this.slotIdx = slotIdx;
	}

	public PageID getPageId() {
		return pageId;
	}

	public int getSlotIdx() {
		return slotIdx;
	}
}