package jemstone.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.artfulbits.aiCharts.Base.ChartAxisScale;
import com.artfulbits.aiCharts.Base.ChartPoint;
import com.artfulbits.aiCharts.Base.ChartRenderArgs;
import com.artfulbits.aiCharts.Base.ChartType;
import com.artfulbits.aiCharts.Base.DoubleRange;
import com.artfulbits.aiCharts.Enums.CoordinateSystem;

/**
 * Rose chart - circular graph, which builds by "pie slices" where Y value defines radius
 * of the slice.
 *
 * @author Baydalka Volodymyr
 */
public class RoseChartType extends ChartType {
  public static final RoseChartType INSTANCE = new RoseChartType();

  /**
   * Paint to draw chart
   */
  private final Paint paint = new Paint();

  /*
   * (non-Javadoc)
   *
   * @see com.artfulbits.aiCharts.Base.ChartType#getRequiredCoordinateSystem()
   */
  @Override
  public CoordinateSystem getRequiredCoordinateSystem() {
    // Rose chart uses polar coordinate system
    return CoordinateSystem.Polar;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.artfulbits.aiCharts.Base.ChartType#isOriginDependent()
   */
  @Override
  public boolean isOriginDependent() {
    // Rose chart draws to the Y origin
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.artfulbits.aiCharts.Base.ChartType#isSideBySide()
   */
  @Override
  public boolean isSideBySide() {
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.artfulbits.aiCharts.Base.ChartType#draw(com.artfulbits.aiCharts.Base.ChartRenderArgs
   * )
   */
  @Override
  public void draw(ChartRenderArgs args) {
    // cache necessary values
    Canvas canvas = args.Canvas;
    ChartAxisScale xScale = args.ActualXAxis.getScale();
    ChartAxisScale yScale = args.ActualYAxis.getScale();
    int yIndex = args.Series.getPointDeclaration().YValueIndex;
    // get sector angles offsets
    DoubleRange sbsOffset = args.getSideBySideOffset();

    float centerX = args.Bounds.centerX();
    float centerY = args.Bounds.centerY();
    // compute maximal radius of sectors
    float maxRadius = Math.min(args.Bounds.width(), args.Bounds.height()) / 2;
    RectF arcRect = new RectF();

    for (ChartPoint point : args.Series.getPointsCache()) {
      // get relative radius of a sector
      double yPos = yScale.valueToCoefficient(point.getY(yIndex));

      // get relative angles of the sector
      double xPos1 = xScale.valueToCoefficient(point.getX() + sbsOffset.Minimum);
      double xPos2 = xScale.valueToCoefficient(point.getX() + sbsOffset.Maximum);

      // compute absolute values
      float radius = (float) (maxRadius * yPos);
      float angle1 = (float) (360 * xPos1);
      float angle2 = (float) (360 * xPos2);

      // compute a sector arc rectangle
      arcRect.set(centerX-radius, centerY-radius, centerX+radius, centerY+radius);

      paint.setAntiAlias(true);
      paint.setStyle(Style.FILL);
      paint.setColor(point.getBackColor());

      canvas.drawArc(arcRect, angle1, angle2-angle1, true, paint);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.artfulbits.aiCharts.Base.ChartType#getName()
   */
  @Override
  public String getName() {
    // Just a name
    return "Rose";
  }
}
