import {BedItemFeatureRenderer} from './drawing';
import {FeatureRenderer} from '../../../../gene/internal/renderer/features';

export default class BEDFeatureRenderer extends FeatureRenderer {

    _bedItemFeatureRenderer = null;

    constructor(config) {
        super(config);
        this._bedItemFeatureRenderer = new BedItemFeatureRenderer(config,
            ::this.registerLabel,
            ::this.registerDockableElement,
            ::this.registerFeaturePosition);
    }

    get renderers() {
        return [this._bedItemFeatureRenderer];
    }

    get defaultRenderer() {
        return this._bedItemFeatureRenderer;
    }
}