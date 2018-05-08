package com.harvestasm.apm.base.pikcer;

public interface ActionModelInterface<T extends ItemModelInterface> {
    void toggleSelected(T itemModel);
    boolean isSelect(T itemModel);
}
